package com.roha.movies;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.roha.movies.domain.PlayDTO;
import org.junit.jupiter.api.Test;
import org.springframework.util.FileCopyUtils;

import java.io.FileReader;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CityResourceTest {

    @Test
    public void loadingCities() throws Exception {
/*
        ResultActions result = mockMvc.perform(get("/cities"));
        result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value("alblasserdam"))
                .andExpect(jsonPath("$[0].href").value("https://www.filmladder.nl/alblasserdam"))
                .andExpect(jsonPath("$[0].name").value("Alblasserdam"));
*/
    }

    @Test
    public void loadingHaarlem() throws Exception {
/*
        ResultActions result = mockMvc.perform(get("/cities/haarlem"));
//        result.andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$[0].movie.id").value( "kung-fu-panda-4-ov-2024"));

        BaseDataLoader baseDataLoader = new BaseDataLoader();
        String url = baseDataLoader.getUrl("all_plays.json").get();
        String jsonContent = FileCopyUtils.copyToString(new FileReader(url));
        MockHttpServletResponse response = result.andReturn().getResponse();
        String responseAsString = response.getContentAsString();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        TypeFactory typeFactory = objectMapper.getTypeFactory();
        List<PlayDTO> actual_Plays = objectMapper.readValue(responseAsString, typeFactory.constructCollectionType(List.class, PlayDTO.class));
        assertThat(actual_Plays.get(0).movieDTO().movieId()).isEqualTo("kung-fu-panda-4-ov-2024");
        List<PlayDTO> expected_Plays = objectMapper.readValue(jsonContent, typeFactory.constructCollectionType(List.class, PlayDTO.class));
        assertThat(actual_Plays.size()).isEqualTo(expected_Plays.size());
        for (int i = 0; i < expected_Plays.size(); i++) {
            PlayDTO expectedplay =  expected_Plays.get(i);
            if(expectedplay.id().startsWith("el-paraiso")
                    || expectedplay.id().startsWith("c-e-ancora-domani")
                    || expectedplay.id().startsWith("les-indesirables")
                    || expectedplay.id().startsWith("orphee")
                    || expectedplay.id().startsWith("un-metier-serieux")){
                //weird character bug in these movies titles
                continue;
            }
            assertThat(expectedplay).isEqualTo(actual_Plays.get(i));
            assertThat(expectedplay.movieDTO().href()).isEqualTo(actual_Plays.get(i).movieDTO().href());
        }
*/
    }

    @Test
    public void load_Movie() throws Exception {
/*
        String requestedMovie =
                """
                            {
                            "movie-id": "nothing",
                            "href": "nothing.nl"
                            }
                        """;
        String  bogusMovieResponse = """
                {
                "id":"nothing","title":"nothing","href":"nothing.nl","rating":"unknown","content":"bogus content for nothing","imageHref":"","duration":42,"titleAddOn":""
                }
              """;

        ResultActions result = this.mockMvc.perform(
                post("/movie")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestedMovie));
        result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(bogusMovieResponse));


    }
*/
    }
}
