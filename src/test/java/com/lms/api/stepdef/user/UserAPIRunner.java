package com.lms.api.stepdef.user;

/*
 * Author : Diana
 * 
 */
import org.junit.runner.RunWith;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith(Cucumber.class)
@CucumberOptions(features = "src/test/resources/features/",
					 glue = {"com.lms.api.stepdef.user" },
					 //tags = "@user",
					 
monochrome=true,
dryRun = false,
plugin = {"pretty","com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:"}
)

public class UserAPIRunner {

}
