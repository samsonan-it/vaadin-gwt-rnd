# Playing with Vaadin/Vaadin+Viritin/Vaadin+GMaps+GWT/Spring/Spring Data 

## Description

Просто эксперименты с разными подходами с использованием Vaadin - как чистыми, так и с плагинами и доп.библиотеками.

Это грязный пример, тут нет тестов и обработки ошибок. Не все данные сохраняются в БД.

## Особенности
- Два варианта реализации простого CRUD. Первый - чистый Vaadin, но с использованием EventBus для декаплинга таблицы и формы. Второй - Vaadin + Viritin для упрощения работы с формой, но при этом сильная связанность между таблицей и формой через простой колбэк.

## How To Run

0. Provide correct google.maps.api key in application.properties 
1. Create jar:
```
mvn package
```
2. Run app:
```
java -jar vaadin-gwt-rnd-0.0.1-SNAPSHOT.jar
```
