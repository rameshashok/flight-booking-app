package com.example.flightbooking.service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AviaStackResponse {
    private List<AviaStackFlight> data;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class AviaStackFlight {
        @JsonProperty("flight_date")
        private String flightDate;

        @JsonProperty("flight_status")
        private String flightStatus;

        private Departure departure;
        private Arrival arrival;
        private Airline airline;
        private Flight flight;

        @Data
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Departure {
            private String airport;
            private String iata;
            private String scheduled;
        }

        @Data
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Arrival {
            private String airport;
            private String iata;
            private String scheduled;
        }

        @Data
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Airline {
            private String name;
            private String iata;
        }

        @Data
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Flight {
            private String iata;
            private String number;
        }
    }
}
