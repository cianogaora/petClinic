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
#
@tag1
Feature: Add and update Owner


	@EmptyFirstName
  Scenario: Add an owner with empty First Name field 
  	Given I am on the Add Owner page
    When I enter nothing in First Name
    And I enter "Hackett" as Last Name 
    And I enter "123 Rue Lemons" in Address 
    And I enter "Montréal" in City
    And I enter "0871234567" in Telephone
    And I click Add Owner
    Then A message should appear under first name saying "must not be empty"	
    
	@EmptyLastName
  Scenario: Add an owner with empty Last Name field  
  	Given I am on the Add Owner page
  	When I enter "Lochlann" as First Name
    And I enter nothing in Last Name
    And I enter "123 Rue Lemons" in Address 
    And I enter "Montréal" in City
    And I enter "0871234567" in Telephone
    And I click Add Owner
    Then A message should appear under Last Name saying "must not be empty"
	
    
  @EmptyCity
  Scenario: Add an owner with empty City field  
  	Given I am on the Add Owner page
  	When I enter "Lochlann" as First Name 
    And I enter "Hackett" as Last Name 
    And I enter "123 Rue Lemons" in Address 
    And I enter nothing in City
    And I enter "0871234567" in Telephone
    And I click Add Owner
    Then A message should appear under City saying "must not be empty"
  
  @EmptyTelephone
  Scenario: Add an owner with empty Telephone field  
  	Given I am on the Add Owner page
  	When I enter "Lochlann" as First Name 
    And I enter "Hackett" as Last Name 
    And I enter "123 Rue Lemons" in Address 
    And I enter "Montréal" in City
    And I enter nothing in the Telephone field
    And I click Add Owner
    Then A message should appear under Telephone saying "numeric value out of bounds"

  @InvalidTelephone
  Scenario: Add an owner with invalid Telephone number
  	Given I am on the Add Owner page
  	When I enter "Lochlann" as First Name 
    And I enter "Hackett" as Last Name 
    And I enter "123 Rue Lemons" in Address 
    And I enter "Montréal" in City
    And I enter "1234567890123" in Telephone
    And I click Add Owner
    Then A message should appear under Telephone saying "numeric value out of bounds"

	@AddValidOwner
	Scenario: Add valid owner 
  	Given I am on the Add Owner page
    When I enter "Lochlann" as First Name 
    And I enter "Hackett" as Last Name 
    And I enter "123 Rue Lemons" in Address 
    And I enter "Montréal" in City
    And I enter "0871234567" in Telephone
    And I click Add Owner
    Then An owner with this information should appear on the next page
    
  @EmptyAddress
  Scenario: Add an owner with empty Address field  
  	Given I am on the Add Owner page
  	When I enter "Lochlann" as First Name 
    And I enter "Hackett" as Last Name 
    And I enter nothing in Address
    And I enter "Montréal" in City
    And I enter "0871234567" in Telephone
    And I click Add Owner
    Then A message should appear under Address saying "must not be empty"
    
  @UpdateValidOwner
	Scenario: Update valid owner 
  	Given I am on the Edit Owner page for owner id "1"
    When I enter "Jacob" as First Name 
    And I enter "Beasly" as Last Name 
    And I enter "123 Rue Lemons" in Address 
    And I enter "Montréal" in City
    And I enter "0871234567" in Telephone
    And I click Update Owner
    Then An owner with this information should appear on the next page
    
  @UpdateEmptyAddress
  Scenario: Update an owner with empty Address field  
  	Given I am on the Edit Owner page for owner id "1"
  	When I enter "Lochlann" as First Name 
    And I enter "Hackett" as Last Name 
    And I enter nothing in Address
    And I enter "Montréal" in City
    And I enter "0871234567" in Telephone
    And I click Update Owner
    Then A message should appear under Address saying "must not be empty"
  
  @UpdateInvalidTelephone
  Scenario: Add an owner with invalid Telephone number
  	Given I am on the Edit Owner page for owner id "1"
  	When I enter "Lochlann" as First Name 
    And I enter "Hackett" as Last Name 
    And I enter "123 Rue Lemons" in Address 
    And I enter "Montréal" in City
    And I enter "12345678901234553" in Telephone
    And I click Update Owner
    Then A message should appear under Telephone saying "numeric value out of bounds"
  
  @UpdateEmptyCity
  Scenario: Update an owner with empty City field  
  	Given I am on the Edit Owner page for owner id "1"
  	When I enter "Lochlann" as First Name 
    And I enter "Hackett" as Last Name 
    And I enter "321 green st" in Address
    And I enter nothing in City
    And I enter "0871234567" in Telephone
    And I click Update Owner
    Then A message should appear under City saying "must not be empty"
    
  @UpdateEmptyFirstName
  Scenario: Update an owner with empty First Name field  
  	Given I am on the Edit Owner page for owner id "1"
  	When I enter nothing in First Name 
    And I enter "Hackett" as Last Name 
    And I enter "321 green st" in Address
    And I enter "Montreal" in City
    And I enter "0871234567" in Telephone
    And I click Update Owner
    Then A message should appear under first name saying "must not be empty"
    
  @UpdateEmptyLastName
  Scenario: Update an owner with empty Last Name field  
  	Given I am on the Edit Owner page for owner id "1"
  	When I enter "Lochlann" as First Name 
    And I enter nothing in Last Name 
    And I enter "321 green st" in Address
    And I enter "Montreal" in City
    And I enter "0871234567" in Telephone
    And I click Update Owner
    Then A message should appear under Last Name saying "must not be empty"
