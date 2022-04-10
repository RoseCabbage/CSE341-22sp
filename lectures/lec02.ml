let x = 3
let y = 1 + 2
let z = x + y + 4
let w = if y < z then 42 else 17
let a = if y > z then x + 39 else z + 7
let b = -11
let c = if b < 0 then -b else b
let d: int = Int.abs(b)

let myabs((n: int)) = 
  if n < 0
  then -n
  else n

let e = myabs(-7)

let max((x: int), (y: int)) = 
  if x < y then y else x

let f = max(10, 3)
let g = max(7, 42)

let avg((x: int), (y: int)) = 
  (x + y) / 2

let h = avg(2, 4)
let i = avg(2, 3)
let j = avg(2, 2)
let k = 1.0
let l = k +. 2.0

let avg_float((x: float), (y: float)) =
  (x +. y) /. 2.0

let m = avg_float(2.0, 4.0)
let n = avg_float(2.0, 3.0)

let rec pow((x: int), (y: int)) = 
  if y = 0
  then 1
  else x * pow(x, y - 1) 

let o = pow(10, 4)
let p = pow(2, 3)

let q: int * bool = (x, false)
let r = fst q
let s = snd q
let t = if snd(q) then fst(q) else 42

let swap((p: int * int)) = 
  (snd(p), fst(p))

let u = swap(3, 10)
let v = [1; 2; 3]
let w = []
let foo = (1, 2, 3)
let bar: int * float * bool = (1, 2.0, true)