Feature: Search for categories
  In order to check details in each category
  As a tester
  I want to confirm the details are correct

  Scenario: Search details of category
    Given a category with number 6327
    When I search for detail information
    Then the name is "Carbon credits"
    Then the value of CanRelist is true
    Then the promotions element with name "Gallery" contains description "2x larger image"
