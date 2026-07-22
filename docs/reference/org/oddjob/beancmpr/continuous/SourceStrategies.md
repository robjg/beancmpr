[HOME](../../../../README.md)
# didocmpr:continuous-strategy

Provides a way to configure a Strategy for comparing
sources for Oddjob. Current strategies are:

- ONE_FOR_ONE: Trys to match each X to each Y
- MANY_FOR_MANY: Any number of Xs will Match any number of Ys. For instance if you get 2 Xs that match a single Y, then 2 matches are reported, as opposed to the ONE_FOR_ONE strategy where the second X would result in a missing Y result. One use case is comparing a ticking price stream with a throttled price stream to ensure the throttled stream had approximately the same data within a tolerance period.


### Property Summary

| Property | Description |
| -------- | ----------- |
| [name](#propertyname) |  | 


### Property Detail
#### name <a name="propertyname"></a>

<table style='font-size:smaller'>
      <tr><td><i>Configured By</i></td><td>ATTRIBUTE</td></tr>
      <tr><td><i>Access</i></td><td>READ_WRITE</td></tr>
</table>




-----------------------

<div style='font-size: smaller; text-align: center;'>(c) R Gordon Ltd 2005 - Present</div>
