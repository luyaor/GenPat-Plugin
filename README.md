# GenPat-Plugin

🚀 News: GenPater is now [on Jetbrains Marketplace](https://plugins.jetbrains.com/plugin/15935-genpater)! Just search *GenPater* in Plugin Marketplace to try it out!


### Pipeline

![pipeline](other/pipeline.png)



### Demo

![demo](other/demo-only-apply.gif)



### Usage

*Note: Currently, it only supports with transformation at function(method) level.*

Trace (for the transformation generation):
  
  1. Choose a function (let cursor inside the function).
  
  2. Start with your modification on the function.
  
      Way A: Click *Trace* button in pop-up menu (right mouse click), or `Shift + Alt + -/_`, then edit in popup editor.
      
      Way B: Start with  `Shift + Alt + [` , then edit in IDE, finally end with `Shift + Alt + ]`.



Apply (for the transformation application):
  1. Choose a target function (let cursor inside the function).
  2. Click *Apply* button in pop-up menu (right mouse click) or  `Shift + Alt + =/+`.

Global Search
  1. Click *Tools->GenPat->GlobalSearch*.
  2. Popup Editor will show all matched results in current file (contains both before and after changing), Click *Replace* for the candidates you would like to apply.

### TODO

1. Extend global search to all project scope
2. User setting for multi candidates
4. Support with method signature modification

### Related Project

https://github.com/xgdsmileboy/GenPat


### Publication

Jiajun Jiang, Luyao Ren, Yingfei Xiong, Lingming Zhang. [Inferring Program Transformations From
Singular Examples via Big Code](http://luyaoren.com/wp-content/uploads/ASE19-GENPAT.pdf). In Proceedings of the 34th IEEE/ACM International Conference on
Automated Software Engineering (**ASE ’19**)

