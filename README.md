# ReactJavaLab1

# lab 2

## 1
- [x] В один из методов, использовавшийся для сбора статистики, добавить возможность задать задержку, имитирующую задержку получения результата, например из базы данных. К примеру,
  был метод getName(), в который нужно добавить параметр getName(long delay)

## 2
- [x] Заменить последовательный стрим, собирающий статистику из лабораторной №1, на параллельный. 
- [x] ? Поменять итоговую коллекцию, где собирается результат, на соответствующую потокобезопасную. 
- [x] Измерить производительность для разного количества элементов с дополнительной задержкой и без задержки. 
- [ ] Для случаев с задержкой и без найти количество элементов, при котором сбор статистики последовательным и 
  параллельным стримами дают одинаковую скорость выполнения.

## 3
- [x] Оптимизировать параллельный сбор статистики, реализовав собственный ForkJoinPool или Spliterator. Измерить
  производительность своего варианта.

## 4
- [ ] Измерения производительности выполнять с помощью фреймворка JMH.