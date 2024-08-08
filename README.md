# Diplom_2

##Учебный проект от Яндекс Практикума
##10 Спринт - Тестирование REST API методов

## Запуск тестов
Запустить все тесты
> mvn clean test

Запустить тесты определенного класса
> mvn clean test -Dtest=AuthUserTest

Запустить конкретный тест
> mvn clean test -Dtest=AuthUserTest#userCanLoginTest

Запустить тест + сгенерировать отчет
> mvn clean test -Dtest=AuthUserTest#userCanLoginTest allure:report

Запустить тест + сгенерировать и захостить отчет
> mvn clean test -Dtest=AuthUserTest#userCanLoginTest allure:serve

## Используемый стек
| Инструмент  | Версия |
| ------------- | ------------- |
| Java  | 11  |
| Rest Assured  | 5.4.0  |
| JUnit  |  5.9.1|
| Allure  | 2.13.1|
| Faker  | 1.0.2  |
| Lombok  | 1.18.28  |
| Gson  | 2.10.1  |
| Maven surefire plugin  | 3.0.0-M4 |
| Json schema validator  | 4.4.0  |