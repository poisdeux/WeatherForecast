/*
 * Copyright 2016 Martijn Brekhof. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.weatherforecast.testHelpers;

/**
 * Created by martijn on 18/08/16.
 */
public class FlakyTest {
    public interface Test {
        void execute();
    }

    private int runs;
    private Test test;

    /**
     * Use FlakyTest to run tests multiple times and only fail if all runs fail.
     * This can be used to test code that sometimes fails because some conditions do not hold
     * and you are unable to fix the environment to make sure the conditions are always correct.
     * @param runs the number of runs that should fail before the test should fail
     * @param test the code that should be tested. To indicate the test fails you must throw
     * an {@link AssertionError}.
     */
    public FlakyTest(int runs, Test test) {
        this.runs = runs;
        this.test = test;
    }

    /**
     * Runs the test set through the {@link FlakyTest} constructor.
     * The {@link AssertionError}s are caught until the amount of runs exceed
     * {@link FlakyTest#runs}. The last thrown {@link AssertionError} is rethrown to indicate a
     * test failure.
     * @throws AssertionError
     */
    public void run() throws AssertionError {
        try {
            test.execute();
        } catch (AssertionError e) {
            if (--runs > 0) {
                run();
            } else {
                throw e;
            }
        }
    }
}
