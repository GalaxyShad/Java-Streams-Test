# ReactJava ITMO Course Tasks

# lab 2

## 1
- [x] В один из методов, использовавшийся для сбора статистики, добавить возможность задать задержку, 
  имитирующую задержку получения результата, например из базы данных. К примеру,
  был метод getName(), в который нужно добавить параметр getName(long delay)

## 2
- [x] Заменить последовательный стрим, собирающий статистику из лабораторной №1, на параллельный. 
- [x] Поменять итоговую коллекцию, где собирается результат, на соответствующую потокобезопасную. 
- [x] Измерить производительность для разного количества элементов с дополнительной задержкой и без задержки. 
- [x] Для случаев с задержкой и без найти количество элементов, при котором сбор статистики последовательным и 
  параллельным стримами дают одинаковую скорость выполнения.

## 3
- [x] Оптимизировать параллельный сбор статистики, реализовав собственный ForkJoinPool или Spliterator. Измерить
  производительность своего варианта.

## 4
- [x] Измерения производительности выполнять с помощью фреймворка JMH.


# lab 3

## 1
- [x] Реализовать подсчет статистики, аналогичный использованному в лабораторной работе №2, 
  с помощью реактивных потоков Observable на RxJava. 
  Должна обеспечиваться многопоточная асинхронная обработка с использованием Scheduler.

## 2
- [x] Провести сравнение производительности обработки 500 и 2000 элементов с включенной задержкой 
  для параллельных потоков (из лабы 2) и для реактивных потоков. 
- [x] Необходимо добиться, чтобы производительность реактивных потоков была такой же или лучше, 
  чем при использовании параллельных потоков.

```
MeasuramentStreamJmhTest.measureRxJava             0              500           true  avgt    5  0,079 ± 0,001  ms/op
MeasuramentStreamJmhTest.measureRxJava             0             2000           true  avgt    5  0,128 ± 0,003  ms/op
MeasuramentStreamJmhTest.measureRxJava             2              500           true  avgt    5  0,155 ± 0,001  ms/op
MeasuramentStreamJmhTest.measureRxJava             2             2000           true  avgt    5  0,217 ± 0,005  ms/op
MeasuramentStreamJmhTest.measureStream             0              500           true  avgt    5  1,289 ± 0,045  ms/op
MeasuramentStreamJmhTest.measureStream             0             2000           true  avgt    5  5,550 ± 2,577  ms/op
MeasuramentStreamJmhTest.measureStream             2              500           true  avgt    5  1,811 ± 0,357  ms/op
MeasuramentStreamJmhTest.measureStream             2             2000           true  avgt    5  6,464 ± 0,979  ms/op
```

## 3
- [x] Отключить задержку при создании элементов. 
- [x] Реализовать собственный Subscriber для подсчета статистики, регулирующий скорость поступления элементов из потока.
- [x] Генерацию элементов с поддержкой backpressure производить асинхронно с помощью Flowable. 

```
Benchmark                                       (delayNanos)  (ledStripCount)  (useParallel)  Mode  Cnt    Score    Error  Units
MeasuramentStreamJmhTest.measureRxJava                     0              500           true  avgt    5    0,106 ±  0,021  ms/op
MeasuramentStreamJmhTest.measureRxJava                     0             2000           true  avgt    5    0,182 ±  0,015  ms/op
MeasuramentStreamJmhTest.measureRxJava                     0           110000           true  avgt    5   14,750 ±  0,175  ms/op
MeasuramentStreamJmhTest.measureRxJavaFlowable             0              500           true  avgt    5    0,091 ±  0,003  ms/op
MeasuramentStreamJmhTest.measureRxJavaFlowable             0             2000           true  avgt    5    0,326 ±  0,011  ms/op
MeasuramentStreamJmhTest.measureRxJavaFlowable             0           110000           true  avgt    5   26,622 ± 11,899  ms/op
MeasuramentStreamJmhTest.measureStream                     0              500           true  avgt    5    1,299 ±  0,031  ms/op
MeasuramentStreamJmhTest.measureStream                     0             2000           true  avgt    5    5,119 ±  0,036  ms/op
MeasuramentStreamJmhTest.measureStream                     0           110000           true  avgt    5  290,139 ± 16,235  ms/op
```

- [x] Убедиться, что при большом количестве элементов (больше 100000) система работает стабильно и без задержек.

```
Benchmark                                       (delayNanos)  (ledStripCount)  (useParallel)  Mode  Cnt   Score   Error  Units
MeasuramentStreamJmhTest.measureRxJavaFlowable             0           110000           true  avgt    4  21,093 ± 1,100  ms/op
MeasuramentStreamJmhTest.measureRxJavaFlowable             0           200000           true  avgt    4  41,761 ± 0,853  ms/op
MeasuramentStreamJmhTest.measureRxJavaFlowable             0           300000           true  avgt    4  61,409 ± 4,814  ms/op
```

