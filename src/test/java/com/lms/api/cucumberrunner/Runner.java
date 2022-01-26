package com.lms.api.cucumberrunner;

import org.junit.runner.RunWith;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

/**
 * Main Runner class to run all cucumber feature files
 *
 */
@RunWith(Cucumber.class)
@CucumberOptions(features = "src/test/resources/features/",
					 glue = {"com/lms/api/stepdef/user", "com/lms/api/stepdef/skillmap", "com/lms/api/stepdef/skills"},
					

monochrome=true,
dryRun = false,
plugin = {"pretty","json:target/Cucumber.json",
		"com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:",
	"timeline:test-output-thread/"}

	)


public class Runner {

}