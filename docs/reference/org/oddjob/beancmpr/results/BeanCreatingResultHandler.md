[HOME](../../../../README.md)
# beancmpr:bean-results

Creates result beans for matches and adds them
to a collection.

### Property Summary

| Property | Description |
| -------- | ----------- |
| [factoryBuilder](#propertyfactorybuilder) | Allow For a different `org.oddjob.beancmpr.results.ResultBeanFactory`. | 
| [ignoreMatches](#propertyignorematches) | If true then result beans will not be created when a comparison results in a match. | 
| [out](#propertyout) | A collection that result beans will be added to. | 
| [xPropertyPrefix](#propertyxpropertyprefix) | This will be prefixed to property names in the result bean. | 
| [yPropertyPrefix](#propertyypropertyprefix) | This will be prefixed to property names in the result bean. | 


### Property Detail
#### factoryBuilder <a name="propertyfactorybuilder"></a>

<table style='font-size:smaller'>
      <tr><td><i>Configured By</i></td><td>ELEMENT</td></tr>
      <tr><td><i>Access</i></td><td>READ_WRITE</td></tr>
      <tr><td><i>Required</i></td><td>No. Defaults to 
 {@link SimpleResultBeanFactoryBuilder}.</td></tr>
</table>

Allow For a different
`org.oddjob.beancmpr.results.ResultBeanFactory`.

#### ignoreMatches <a name="propertyignorematches"></a>

<table style='font-size:smaller'>
      <tr><td><i>Configured By</i></td><td>ATTRIBUTE</td></tr>
      <tr><td><i>Access</i></td><td>READ_WRITE</td></tr>
      <tr><td><i>Required</i></td><td>No. Defaults to false.</td></tr>
</table>

If true then result beans will not be created
when a comparison results in a match. If false result beans
for all comparisons will be created.

#### out <a name="propertyout"></a>

<table style='font-size:smaller'>
      <tr><td><i>Configured By</i></td><td>ELEMENT</td></tr>
      <tr><td><i>Access</i></td><td>READ_WRITE</td></tr>
      <tr><td><i>Required</i></td><td>No.</td></tr>
</table>

A collection that result beans will be added to.

#### xPropertyPrefix <a name="propertyxpropertyprefix"></a>

<table style='font-size:smaller'>
      <tr><td><i>Configured By</i></td><td>ATTRIBUTE</td></tr>
      <tr><td><i>Access</i></td><td>READ_WRITE</td></tr>
      <tr><td><i>Required</i></td><td>No.</td></tr>
</table>

This will be prefixed to property names in
the result bean.

#### yPropertyPrefix <a name="propertyypropertyprefix"></a>

<table style='font-size:smaller'>
      <tr><td><i>Configured By</i></td><td>ATTRIBUTE</td></tr>
      <tr><td><i>Access</i></td><td>READ_WRITE</td></tr>
      <tr><td><i>Required</i></td><td>No.</td></tr>
</table>

This will be prefixed to property names in
the result bean.


-----------------------

<div style='font-size: smaller; text-align: center;'>(c) R Gordon Ltd 2005 - Present</div>
