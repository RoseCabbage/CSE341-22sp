let x = (1, 2, 3)
let y = 
  match x with
  | (a, b, c) -> a + b + c

let z = 
  let (a, b, c) = x
  in a + b + c

let sum_triple ((tr: int * int * int)): int = 
  let (a, b, c) = tr in
  a + b + c

let sum_triple2 (a, b, c): int = 
  a + b + c

let d = sum_triple((1, 2, 3))
let e = sum_triple(1, 2, 3)
let f = sum_triple2((1, 2, 3))
let g = sum_triple2(1, 2, 3)

let rotate_left_triple (a, b, c) = (c, b, a)
let rotate_right_triple (tr) = rotate_left_triple(rotate_left_triple(tr))

let rec rev(l: 'a list): 'a list = 
  match l with
  | [] -> []
  | x :: xs -> rev(xs) @ [x]

let rev2 (l: 'a list): 'a list = 
  let rec loop ((l: 'a list), (acc: 'a list)): 'a list = 
    match l with
    | [] -> acc
    | x :: xs -> loop(xs, x :: acc)
  in
  loop(l, [])

exception MyException
exception MyOtherException of string * int

let oh_no () = 
  raise MyException
let oh_no_with_args () = 
  raise (MyOtherException ("hello", 17))

let catch_example () = 
  try
    oh_no ()
  with
  MyException -> "caught!"

let catch_example_with_args () =
  try
    oh_no_with_args ()
  with
  MyOtherException (s, n) -> "caught info: " ^ s ^ " " ^ Int.to_string n

let catch_example_multiple b = 
  try
    if b
    then
      oh_no ()
    else
      oh_no_with_args ()
  with 
  | MyException -> "caught MyException"
  | MyOtherException (s, n) -> "caught info: " ^ s ^ " " ^ Int.to_string n

let catch_all b = 
  try
    if b
    then 
      oh_no ()
    else
      oh_no_with_args () 
  with
  | _ -> "caught something"

let catch_all3 b = 
  try
    if b
    then
      oh_no ()
    else
      oh_no_with_args ()
  with
  | e -> e