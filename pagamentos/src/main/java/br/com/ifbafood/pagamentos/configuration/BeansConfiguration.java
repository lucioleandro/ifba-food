package br.com.ifbafood.pagamentos.configuration;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeansConfiguration {

    @Bean
    public ModelMapper createModelMapper() {
        ModelMapper mapper = new ModelMapper();
        //mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);

        return mapper;
    }
}
