[HOME](../../../../README.md)
# beancmpr:comparers-by-type

Allows comparers to be defined by the type of
thing they are comparing.


Comparers can be listed using the <code>comparers</code> property
which provides a new configuration for all types the comparer is for,
or the <code>specialisations</code> property which allows a comparer
to be targeted at a particular class.

### Property Summary

| Property | Description |
| -------- | ----------- |
| [classLoader](#propertyclassloader) | The class loader to use to resolve the type of a specialisation. | 
| [comparers](#propertycomparers) | Comparers listed. | 
| [specialisations](#propertyspecialisations) | Comparers specialised by class name. | 


### Property Detail
#### classLoader <a name="propertyclassloader"></a>

<table style='font-size:smaller'>
      <tr><td><i>Configured By</i></td><td>ELEMENT</td></tr>
      <tr><td><i>Access</i></td><td>WRITE_ONLY</td></tr>
      <tr><td><i>Required</i></td><td>No. This will be set from Oddjob automatically.</td></tr>
</table>

The class loader to use to resolve the type
of a specialisation.

#### comparers <a name="propertycomparers"></a>

<table style='font-size:smaller'>
      <tr><td><i>Configured By</i></td><td>ELEMENT</td></tr>
      <tr><td><i>Access</i></td><td>WRITE_ONLY</td></tr>
      <tr><td><i>Required</i></td><td>No.</td></tr>
</table>

Comparers listed. A comparer here will override
the default comparer for a give type.

#### specialisations <a name="propertyspecialisations"></a>

<table style='font-size:smaller'>
      <tr><td><i>Configured By</i></td><td>ELEMENT</td></tr>
      <tr><td><i>Access</i></td><td>WRITE_ONLY</td></tr>
      <tr><td><i>Required</i></td><td>No.</td></tr>
</table>

Comparers specialised by class name. The key
povides the class a comparer is for.


-----------------------

<div style='font-size: smaller; text-align: center;'>(c) R Gordon Ltd 2005 - Present</div>
