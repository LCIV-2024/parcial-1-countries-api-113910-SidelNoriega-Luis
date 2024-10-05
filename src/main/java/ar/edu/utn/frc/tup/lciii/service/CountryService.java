package ar.edu.utn.frc.tup.lciii.service;

import ar.edu.utn.frc.tup.lciii.dtos.common.CountryDTO;
import ar.edu.utn.frc.tup.lciii.entities.CountryEntity;
import ar.edu.utn.frc.tup.lciii.model.Country;
import ar.edu.utn.frc.tup.lciii.repository.CountryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CountryService {

        private final CountryRepository countryRepository;

        @Autowired
        private final RestTemplate restTemplate;

        public List<Country> getAllCountries() {
                String url = "https://restcountries.com/v3.1/all";
                List<Map<String, Object>> response = restTemplate.getForObject(url, List.class);
                return response.stream().map(this::mapToCountry).collect(Collectors.toList());
        }

        /**
         * Agregar mapeo de campo cca3 (String)
         * Agregar mapeo campos borders ((List<String>))
         */
        private Country mapToCountry(Map<String, Object> countryData) {
                Map<String, Object> nameData = (Map<String, Object>) countryData.get("name");
                return Country.builder()
                        .name((String) nameData.get("common"))
                        .population(((Number) countryData.get("population")).longValue())
                        .area(((Number) countryData.get("area")).doubleValue())
                        .code((String) countryData.get("cca3"))
                        .region((String) countryData.get("region"))
                        .borders((List<String>) countryData.get("borders"))
                        .languages((Map<String, String>) countryData.get("languages"))
                        .build();
        }

        private CountryDTO mapToDTO(Country country) {
                return new CountryDTO(country.getCode(), country.getName());
        }

        public List<CountryDTO> getAllCountriesDTO(String name, String code) {
                List<Country> countries = getAllCountries();
                if ((name == null || name.isEmpty()) && (code == null || code.isEmpty())) {
                        return countries.stream().map(this::mapToDTO).toList();
                }
                if (code != null && !code.isEmpty()) {
                        return countries.stream().filter(country -> country.getCode().equalsIgnoreCase(code)).map(this::mapToDTO).toList();
                } else {
                        return countries.stream().filter(country -> country.getName().equalsIgnoreCase(name)).map(this::mapToDTO).toList();
                }
        }

        public List<CountryDTO> getCountriesByContinent(String continent) {
                List<Country> countries = getAllCountries();
                return countries.stream().filter(country -> country.getRegion().equalsIgnoreCase(continent)).map(this::mapToDTO).toList();
        }

        public List<CountryDTO> getCountriesByLanguaje(String language) {
                List<Country> countries = getAllCountries();
                return countries.stream()
                        .filter(country -> country.getLanguages() != null && country.getLanguages().containsValue(language))
                        .map(this::mapToDTO)
                        .toList();
        }

        public CountryDTO getCountryWithMostBorders() {
                List<Country> countries = getAllCountries();
                Country countryWithMostBorders = countries.stream()
                        .filter(country -> country.getBorders() != null)
                        .max(Comparator.comparingInt(c -> c.getBorders().size()))
                        .orElse(null);
                return countryWithMostBorders != null ? mapToDTO(countryWithMostBorders) : null;
        }



        public List<CountryDTO> saveCountries(Integer amountOfCountryToSave) {
                if (amountOfCountryToSave > 10) {
                        throw new IllegalArgumentException("The amount of countries to save cannot be greater than 10");
                }
                List<Country> countries = getAllCountries();

                Collections.shuffle(countries);
                List<Country> countriesToSave = countries.subList(0, amountOfCountryToSave);
                List<CountryEntity> countryEntities = countriesToSave.stream().map(country -> {
                        CountryEntity countryEntity = new CountryEntity();
                        countryEntity.setCode(country.getCode());
                        countryEntity.setName(country.getName());
                        return countryEntity;
                }).toList();

                List<CountryEntity> savedCountryEntities = countryRepository.saveAll(countryEntities);
                return savedCountryEntities.stream().map(countryEntity -> {
                        CountryDTO countryDTO = new CountryDTO();
                        countryDTO.setCode(countryEntity.getCode());
                        countryDTO.setName(countryEntity.getName());
                        return countryDTO;
                }).toList();
        }}