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
//        , plugin = {"pretty, io.qameta.allure.cucumberjvm.AllureCucumberJvm"}
)

public class AllFeaturesTests {
}
