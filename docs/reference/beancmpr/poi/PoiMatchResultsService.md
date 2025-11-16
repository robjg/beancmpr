[HOME](../../README.md)
# beancmpr:poi-results

A service that can be used in a Bean Bus to create an Excel
spreadsheet of comparison results. The result will typically be created
using the [beancmpr:match-results](../../org/oddjob/beancmpr/results/MatchResultHandlerFactory.md) as
the Result Handler of an [beancmpr:compare](../../org/oddjob/beancmpr/BeanCompareJob.md).

### Property Summary

| Property | Description |
| -------- | ----------- |
| [autoFilter](#propertyautofilter) | Should filters be applied to the columns. | 
| [autoWidth](#propertyautowidth) | Should the column width expand to fit the data. | 
| [firstColumn](#propertyfirstcolumn) | The position of the first column. | 
| [firstRow](#propertyfirstrow) | The position of the first row. | 
| [name](#propertyname) | The name of the component as seen in Oddjob. | 
| [sheetName](#propertysheetname) | The name of the sheet that will be created. | 
| [to](#propertyto) | An additional consumer to send results to. | 
| [workbook](#propertyworkbook) | The provider of the Excel workbook to write to. | 
| [xPrefix](#propertyxprefix) | The prefix that will be used for the X titles. | 
| [yPrefix](#propertyyprefix) | The prefix that will be used for the Y titles. | 


### Property Detail
#### autoFilter <a name="propertyautofilter"></a>

<table style='font-size:smaller'>
      <tr><td><i>Configured By</i></td><td>ATTRIBUTE</td></tr>
      <tr><td><i>Access</i></td><td>READ_WRITE</td></tr>
      <tr><td><i>Required</i></td><td>No. Defaults to false.</td></tr>
</table>

Should filters be applied to the columns.

#### autoWidth <a name="propertyautowidth"></a>

<table style='font-size:smaller'>
      <tr><td><i>Configured By</i></td><td>ATTRIBUTE</td></tr>
      <tr><td><i>Access</i></td><td>READ_WRITE</td></tr>
      <tr><td><i>Required</i></td><td>No. Defaults to false.</td></tr>
</table>

Should the column width expand to fit the data.

#### firstColumn <a name="propertyfirstcolumn"></a>

<table style='font-size:smaller'>
      <tr><td><i>Configured By</i></td><td>ATTRIBUTE</td></tr>
      <tr><td><i>Access</i></td><td>READ_WRITE</td></tr>
      <tr><td><i>Required</i></td><td>No. Defaults to 1.</td></tr>
</table>

The position of the first column. 1 being the leftmost.

#### firstRow <a name="propertyfirstrow"></a>

<table style='font-size:smaller'>
      <tr><td><i>Configured By</i></td><td>ATTRIBUTE</td></tr>
      <tr><td><i>Access</i></td><td>READ_WRITE</td></tr>
      <tr><td><i>Required</i></td><td>No. Defaults to 1.</td></tr>
</table>

The position of the first row. 1 being the top

#### name <a name="propertyname"></a>

<table style='font-size:smaller'>
      <tr><td><i>Configured By</i></td><td>ATTRIBUTE</td></tr>
      <tr><td><i>Access</i></td><td>READ_WRITE</td></tr>
      <tr><td><i>Required</i></td><td>No.</td></tr>
</table>

The name of the component as seen in Oddjob.

#### sheetName <a name="propertysheetname"></a>

<table style='font-size:smaller'>
      <tr><td><i>Configured By</i></td><td>ATTRIBUTE</td></tr>
      <tr><td><i>Access</i></td><td>READ_WRITE</td></tr>
      <tr><td><i>Required</i></td><td>No.</td></tr>
</table>

The name of the sheet that will be created.

#### to <a name="propertyto"></a>

<table style='font-size:smaller'>
      <tr><td><i>Configured By</i></td><td>ELEMENT</td></tr>
      <tr><td><i>Access</i></td><td>READ_WRITE</td></tr>
      <tr><td><i>Required</i></td><td>No.</td></tr>
</table>

An additional consumer to send results to. If the service
is the middle of Bean Bus pipeline then this will be automatically set to
the next component. Otherwise, might be useful for testing but can otherwise be
ignored.

#### workbook <a name="propertyworkbook"></a>

<table style='font-size:smaller'>
      <tr><td><i>Configured By</i></td><td>ELEMENT</td></tr>
      <tr><td><i>Access</i></td><td>READ_WRITE</td></tr>
      <tr><td><i>Required</i></td><td>Yes.</td></tr>
</table>

The provider of the Excel workbook to write to.
Typically, this will be a <a href="https://github.com/robjg/dido/blob/master/docs/reference/dido/poi/data/PoiWorkbook.md">dido-poi:workbook</a>

#### xPrefix <a name="propertyxprefix"></a>

<table style='font-size:smaller'>
      <tr><td><i>Configured By</i></td><td>ATTRIBUTE</td></tr>
      <tr><td><i>Access</i></td><td>READ_WRITE</td></tr>
      <tr><td><i>Required</i></td><td>No. Defaults to X_.</td></tr>
</table>

The prefix that will be used for the X titles.

#### yPrefix <a name="propertyyprefix"></a>

<table style='font-size:smaller'>
      <tr><td><i>Configured By</i></td><td>ATTRIBUTE</td></tr>
      <tr><td><i>Access</i></td><td>READ_WRITE</td></tr>
      <tr><td><i>Required</i></td><td>No. Defaults to Y_.</td></tr>
</table>

The prefix that will be used for the Y titles.


-----------------------

<div style='font-size: smaller; text-align: center;'>(c) R Gordon Ltd 2005 - Present</div>
