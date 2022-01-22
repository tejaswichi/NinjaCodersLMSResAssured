package com.lms.api.stepdef.skillmap;
import org.junit.runner.RunWith;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith(Cucumber.class)
@CucumberOptions(features = "src/test/resources/features/UserSkillMapDelete.feature", 
						glue = {"com/lms/api/stepdef/skillmap"},
						//tags="@check",
monochrome=true,
dryRun = false,
plugin = {"pretty","com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:"}
)
public class UserSkillMapAPIRunner {

}