package tests;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
//        tags = "@Calculator",
        snippets = CucumberOptions.SnippetType.CAMELCASE,
        features = "src/test/resources/features",
        glue = {"steps"}
        , plugin = {"pretty", "html:target/cucumber-reports/cucumber.html", "json:target/cucumber-reports/cucumber.json"}
)

public class AllFeaturesTest {
}
