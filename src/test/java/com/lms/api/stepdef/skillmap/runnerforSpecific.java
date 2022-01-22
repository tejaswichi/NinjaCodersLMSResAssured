package com.lms.api.stepdef.skillmap;
import org.junit.runner.RunWith;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith(Cucumber.class)
@CucumberOptions(features = "src/test/resources/featurs/UserSkillMapGet.feature", glue = {"com/lms/api/stepdef/skillmap"},
//tags="@Get",
monochrome=true,
dryRun = false
/*plugin = {"pretty","html:target/HtmlReports1/report.html",
  "json:target/JSONReports/report.json",
  "junit:target/JUnitReports/report.xml"}*/
)
public class runnerforSpecific {

}