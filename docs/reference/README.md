# Oddjob Reference

### Jobs

- [beancmpr:compare](org/oddjob/beancmpr/BeanCompareJob.md) - A job that takes two streams of beans and attempts to match the beans according to their properties.
- [beancmpr:poi-results](beancmpr/poi/PoiMatchResultsService.md) - 

### Types

- [beancmpr:array-comparer](org/oddjob/beancmpr/beans/BeanArrayComparerType.md) - Compares an Array of Java Beans.
- [beancmpr:bean-comparer](org/oddjob/beancmpr/beans/BeanComparerType.md) - Provides a definition for how to compare two Beans by the properties of the beans, or how to compare two Objects just by their values.
- [beancmpr:bean-results](org/oddjob/beancmpr/results/BeanCreatingResultHandler.md) - Creates result beans for matches and adds them to a collection.
- [beancmpr:collection-comparer](org/oddjob/beancmpr/beans/IterableBeansComparerType.md) - Compares two Iterables of Java Beans.
- [beancmpr:comparable-comparer](org/oddjob/beancmpr/comparers/ComparableComparer.md) - A generic Comparer for Java Comparables.
- [beancmpr:comparers-by-name](org/oddjob/beancmpr/composite/ComparersByNameType.md) - Allows comparers to be specified by the names of a properties.
- [beancmpr:comparers-by-type](org/oddjob/beancmpr/composite/ComparersByTypeList.md) - Allows comparers to be defined by the type of thing they are comparing.
- [beancmpr:date-comparer](org/oddjob/beancmpr/comparers/DateComparer.md) - A Comparer that will allow a comparison between two Java dates with a millisecond tolerance.
- [beancmpr:equality-comparer](org/oddjob/beancmpr/comparers/EqualityComparer.md) - A comparer that uses the equals method of an object.
- [beancmpr:map-comparer](org/oddjob/beancmpr/beans/MapComparerType.md) - Provides a Comparer that can compare two maps.
- [beancmpr:match-results](org/oddjob/beancmpr/results/MatchResultHandlerFactory.md) - 
- [beancmpr:numeric-comparer](org/oddjob/beancmpr/comparers/NumericComparer.md) - A Comparer for numbers that supports tolerances and provides the comparison as a difference between the numbers and as a percentage change between numbers.
- [beancmpr:text-comparer](org/oddjob/beancmpr/comparers/TextComparer.md) - A comparer for text.
- [didocmpr:collection-comparer](beancmpr/dido/beans/IterableDataComparerType.md) - Compares two Iterables of Generic Data.
- [didocmpr:data-results](beancmpr/dido/results/GenericDataResultHandlerFactory.md) - A Result Handler that will generate results as Generic Data.

-----------------------

<div style='font-size: smaller; text-align: center;'>(c) R Gordon Ltd 2005 - Present</div>
