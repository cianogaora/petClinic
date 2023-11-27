#Author: your.email@your.domain.com
#Keywords Summary :
#Feature: List of scenarios.
#Scenario: Business rule through list of steps with arguments.
#Given: Some precondition step
#When: Some key actions
#Then: To observe outcomes or validation
#And,But: To enumerate more Given,When,Then steps
#Scenario Outline: List of steps for data-driven as an Examples and <placeholder>
#Examples: Container for s table
#Background: List of steps run before each of the scenarios
#""" (Doc Strings)
#| (Data Tables)
#@ (Tags/Labels):To group Scenarios
#<> (placeholder)
#""
## (Comments)
#Sample Feature Definition Template


@tag
Feature: Client Search


Scenario: Search for an Owner
	Given I am on find owners page
	When I search for an owner with last name "Coleman"
	Then Owner with the name "Jean Coleman" should be displayed
	
Scenario: Search for an lower case Owner
	Given I am on find owners page
	When I search for an owner with last name "coleman"
	Then Owner with the name "Jean Coleman" should be displayed

Scenario: Search for all owners
	Given I am on find owners page
	When I hit search without entering a name
	Then All owners in the database should be returned
	
Scenario: Search for an name with multiple occurrences
	Given I am on find owners page
	When I search for an owner with last name "Davis"
	Then All Owners with the name "Davis" should be displayed
	
Scenario: Search for an invalid Owner
	Given I am on find owners page
	When I search for an owner with last name "Obama"
	Then I should be told no such owner exists