package tests;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        tags = "@Calculator",
        snippets = CucumberOptions.SnippetType.CAMELCASE,
        features = "src/tests/resources/features/Calculators.feature",
        glue = {"steps"}
)

public class IdealWeightTests {
}
