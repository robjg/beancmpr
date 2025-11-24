[HOME](../../../../README.md)
# beancmpr:comparers-by-name

Allows comparers to be specified by the names of
a properties. This type is used by comparers such as
[beancmpr:array-comparer](../../../../org/oddjob/beancmpr/beans/BeanArrayComparerType.md), [beancmpr:collection-comparer](../../../../org/oddjob/beancmpr/beans/IterableBeansComparerType.md),
[beancmpr:bean-comparer](../../../../org/oddjob/beancmpr/beans/BeanComparerType.md), or [beancmpr:map-comparer](../../../../org/oddjob/beancmpr/beans/MapComparerType.md).
The name of the property is
given as the key. It must exactly match the incoming field name. If it
doesn't, Beancmpr will silently fall back on the default comparer. *

### Property Summary

| Property | Description |
| -------- | ----------- |
| [comparers](#propertycomparers) | Comparers keyed by property name. | 


### Property Detail
#### comparers <a name="propertycomparers"></a>

<table style='font-size:smaller'>
      <tr><td><i>Configured By</i></td><td>ELEMENT</td></tr>
      <tr><td><i>Access</i></td><td>READ_WRITE</td></tr>
      <tr><td><i>Required</i></td><td>No. But pointless if missing.</td></tr>
</table>

Comparers keyed by property name.


-----------------------

<div style='font-size: smaller; text-align: center;'>(c) R Gordon Ltd 2005 - Present</div>
