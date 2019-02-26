Feature: Search reddit communities by keywords

  Scenario Outline: User searches community by keyword <keyword>
    Given home page is opened
    When user searches <keyword>
    Then top community in search result is <community>
    Examples:
      | keyword | community |
      | cat     | r/cat     |
      | doggo   | r/doggo   |