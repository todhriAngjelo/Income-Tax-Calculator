# Minnesota Income tax Calculator

## Reengineering the legacy code


The goal of this project is to reengineer a legacy Java application. At a glance, serves for the 
income tax calculation of the Minnesota state citizens. The tax calculation accounts for the marital
status of a given citizen, his income, and the amount of money that he has spend, as witnessed by a
set of receipts declared along with the income. The legacy application takes as input txt or xml 
files that contain the necessary data for each citizen. The tax calculation is based on a complex 
algorithm provided by the Minnesota state. The application further produces graphical representation
s of the data in terms of bar and pie charts. Finally. the application produces respective output 
reports in txt or xml.

Legacy code Smells:
- The Taxpayer class has a lot of code duplication.


- The Taxpayer class suffers from [primitive obsession](https://refactoring.guru/smells/primitive-obsession). 
A lot of constants for the income limits and income tax rates, are used to calculate the tax of the Taxpayer. 
The values of these constants depend on the value of the family status attribute. Essentially the family status acts 
as a typecode for different kinds of Taxpayers.


- The different subclasses of Receipt are [lazy classes](https://refactoring.guru/smells/lazy-class). 


- The Database, the InputSystem and the OutputSystem classes do not really follow the object-oriented style. 
The main problem is that all of them just define a bunch of static methods that operate on static class attributes. 
All these static methods and attributes prevent further refactoring that will allow to separate the class responsibilities 
into smaller classes and get rid of code duplication by extracting abstract classes and template methods.


- The InputSystem class has too many responsibilities. It is used to parse two different formats (XML and TXT).


- The OutputSystem class has too many responsibilities. It is used to generate reports in two different formats. 
Moreover, it creates the graphical representation of the data (pie charts, bar charts and so on).


### Reengineering
- We added [Maven](https://maven.apache.org/) to handle the project dependencies, build and test process.
- We fixed all the Smells. See the repo [wiki](https://github.com/AlexandrosAlexiou/Income-Tax-Calculator/wiki) for more details
about the refactoring.

