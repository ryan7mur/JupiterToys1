Question 1
Test url extensions work for each page
Verify user can login with correct credentials; And incorrect credentials yield an error
Check that the user can empty their cart per product and as a whole
Update the product quantities in the cart to verify the subtotal and total changes
Button test to check buttons and links work as expected on each screen
Verify error messages when invalid entries are made to contact form - e.g. letters in phone number field
Verify customer can check out successfully
Validate errors on required fields in checkout screen
Verify error messages when invalid entries are made to checkout form

Question 2
Run sets of regression tests covering various functions simultaneously on different virtual machines via CI tool.
CI Tools can aid in scheduling of the test suite execution around certain times. This won’t speed up test suite time but you can schedule the execution so you have results when you need.
Run test suites in headless mode.
Don’t implement Thread.sleep()  or implicit waits - use explicits waits with timeouts where necessary.
Keep test cases simple and focus on one area of functionality. These tests can be much faster and easier to execute than larger tests. Also easier to debug.
Avoid using xpath selectors where possible - use ID, className, cssSelector etc. instead.

Question 3
It can be difficult to use a BDD approach to test every behaviour of a system. Therefore, the approach should be primarily used on the more important aspects of the system such as prominent functional areas and in user acceptance testing. 
Using a BDD approach to automation should also only be done when there is a clear and full understanding between the dev team, qa team and all stakeholders of the requirements for the behaviour of the software and following the creation of test scenarios. BDD may not be effective if the requirements aren’t meticulously specified

BDD is not as effective when there is less buy in from people within the organisation. Collaboration is essential to clear up any ambiguities from the requirements but if a user can’t be contacted it slows down the process of gaining feedback and developing the test scenarios.
Hard edge cases should not be tested using a BDD approach. Edge cases don’t represent what an end user’s interaction will ideally be with the system and are probably more difficult to create scenarios for.

