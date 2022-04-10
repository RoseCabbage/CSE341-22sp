let x = (1, 2)
let y = (1, 2, 3)
let z = fst(x)
let w = snd(x)

let fst3 (x,_,_) = x
let snd3 (_,x,_) = x
let thd3 (_,_,x) = x

let a = [1; 2; 3]
let b = []
let c = List.tl(a)
let d = 4 :: c
let e = 1 :: 2 :: 3 :: []
let f = [1; 2; 3] @ [4; 5; 6]

let rec countdown ((n: int)) = 
  if n = 0
  then []
  else n :: countdown(n - 1)

let rec sum_list ((l: int list)) = 
  if l = []
  then 0
  else List.hd(l) + sum_list(List.tl(l))

let rec firsts ((l: (int * int) list)) = 
  if l = []
  then []
  else 
    let h = List.hd(l) in
    fst(h) :: firsts(List.tl(l))

let g = firsts([(1, 2); (3, 4); (5, 6)])

let h: int option = None
let i: int option = Some 3
let j: int option = 
  if a = []
  then None
  else Some(List.hd(a))

let celsius_of_fahrenheit (tempF: float) =
  (tempF -. 32.0) *. 5.0 /. 9.0

let is_first_warmmer ((temp1: float), (is_celsius1: bool), 
                      (temp2: float), (is_celsius2: bool)) =
  let temp1C = if is_celsius1 then temp1 else celsius_of_fahrenheit(temp1) in
  let temp2C = if is_celsius2 then temp2 else celsius_of_fahrenheit(temp2) in
  temp1C > temp2C

let is_first_warmmer2 ((temp1: float), (is_celsius1: bool), 
                       (temp2: float), (is_celsius2: bool)) =
  (if is_celsius1 then temp1 else celsius_of_fahrenheit(temp1)) > 
  (if is_celsius2 then temp2 else celsius_of_fahrenheit(temp2))

let is_first_warmmer ((temp1: float), (is_celsius1: bool), 
                      (temp2: float), (is_celsius2: bool)) =
  (let temp1C = if is_celsius1 then temp1 else celsius_of_fahrenheit(temp1) in temp1C) >
  (let temp2C = if is_celsius2 then temp2 else celsius_of_fahrenheit(temp2) in temp2C)
