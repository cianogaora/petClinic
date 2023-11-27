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
Feature: Pet add and update

	@AddValidPet
	Scenario: Add valid pet 
  	Given I am on the New pet page for Owner id "1"
    When I enter "Bill" as Name
    And I enter "2010-02-23" as Birth Date 
    And I enter "cat" as Type 
    And I click Add Pet
    Then A pet with this information should appear on the next page
    
  @DuplicatePet
	Scenario: Add duplicate pet 
  	Given I am on the New pet page for Owner id "1"
    When I enter "Bill" as Name
    And I enter "2010-02-23" as Birth Date 
    And I enter "cat" as Type 
    And I click Add Pet
    Then A message should appear under Name saying "is already in use"
    
  @NoNamePet
	Scenario: Add Pet with no Name 
  	Given I am on the New pet page for Owner id "1"
    When I enter no Name
    And I enter "2010-02-23" as Birth Date 
    And I enter "cat" as Type 
    And I click Add Pet
    Then A message should appear under Name saying "is required"
    
  @NoBirthdayPet
	Scenario: Add pet with no bday 
  	Given I am on the New pet page for Owner id "1"
    When I enter "Jake" as Name
    And I enter nothing as Birth Date 
    And I enter "dog" as Type 
    And I click Add Pet
    Then A message should appear under Birth Date saying "is required"
    
	@UpdateDuplicatePet
	Scenario: Add duplicate pet 
  	Given I am on the edit pet page for Owner id "1" and pet id "1"
    When I enter "Leo" as Name
    And I enter "2010-02-23" as Birth Date 
    And I enter "cat" as Type 
    And I click Add Pet
    Then A message should appear under Name saying "is already in use"
    
  @UpdateNoNamePet
	Scenario: Add Pet with no Name 
  	Given I am on the edit pet page for Owner id "1" and pet id "1"
    When I enter no Name
    And I enter "2010-02-23" as Birth Date 
    And I enter "cat" as Type 
    And I click Add Pet
    Then A message should appear under Name saying "is required"
    
  @UpdateNoBirthdayPet
	Scenario: Add valid pet 
  	Given I am on the edit pet page for Owner id "1" and pet id "1"
    When I enter "Jake" as Name
    And I enter nothing as Birth Date 
    And I enter "dog" as Type 
    And I click Add Pet
    Then A message should appear under Birth Date saying "is required"
    

