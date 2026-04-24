package com.rescureat.integration;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rescureat.model.FoodListing;
import com.rescureat.repository.FoodListingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ReservationsVisibilityIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FoodListingRepository foodListingRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Long dealId;

    @BeforeEach
    void ensureDealExists() {
        FoodListing deal = foodListingRepository.findAll().stream().findFirst().orElseGet(() ->
                foodListingRepository.save(new FoodListing(
                        null,
                        "Test Deal",
                        "desc",
                        "Cafe X",
                        10.0,
                        5.0,
                        43.23,
                        76.93,
                        "District",
                        "Uni"
                ))
        );
        dealId = deal.getId();
    }

    @Test
    void studentACannotSeeStudentBReservations() throws Exception {
        String studentAToken = registerAndGetToken("Student A", "student.a@test.com", "STUDENT");
        String studentBToken = registerAndGetToken("Student B", "student.b@test.com", "STUDENT");

        Long reservationAId = createReservation(studentAToken, dealId);
        Long reservationBId = createReservation(studentBToken, dealId);

        JsonNode studentAList = getReservations(studentAToken);
        assertThat(studentAList.size()).isEqualTo(1);
        assertThat(studentAList.get(0).get("id").asLong()).isEqualTo(reservationAId);
        assertThat(studentAList.get(0).get("id").asLong()).isNotEqualTo(reservationBId);
    }

    @Test
    void cafeOwnerCannotSeeUnrelatedReservations() throws Exception {
        String studentToken = registerAndGetToken("Student C", "student.c@test.com", "STUDENT");
        String ownerToken = registerAndGetToken("Owner A", "owner.a@test.com", "CAFE_OWNER");

        Long studentReservationId = createReservation(studentToken, dealId);
        Long ownerReservationId = createReservation(ownerToken, dealId);

        JsonNode ownerList = getReservations(ownerToken);
        assertThat(ownerList.size()).isEqualTo(1);
        assertThat(ownerList.get(0).get("id").asLong()).isEqualTo(ownerReservationId);
        assertThat(ownerList.get(0).get("id").asLong()).isNotEqualTo(studentReservationId);
    }

    private String registerAndGetToken(String name, String email, String role) throws Exception {
        String payload = """
                {
                  "name": "%s",
                  "email": "%s",
                  "password": "secret123",
                  "role": "%s"
                }
                """.formatted(name, email, role);

        MvcResult result = mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isOk())
                .andReturn();

        JsonNode body = objectMapper.readTree(result.getResponse().getContentAsString(StandardCharsets.UTF_8));
        return body.get("token").asText();
    }

    private Long createReservation(String token, Long requestedDealId) throws Exception {
        String payload = """
                {
                  "dealId": %d
                }
                """.formatted(requestedDealId);

        MvcResult result = mockMvc.perform(post("/api/reservations")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isCreated())
                .andReturn();

        JsonNode body = objectMapper.readTree(result.getResponse().getContentAsString(StandardCharsets.UTF_8));
        return body.get("id").asLong();
    }

    private JsonNode getReservations(String token) throws Exception {
        MvcResult result = mockMvc.perform(get("/api/reservations")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andReturn();
        return objectMapper.readTree(result.getResponse().getContentAsString(StandardCharsets.UTF_8));
    }
}
