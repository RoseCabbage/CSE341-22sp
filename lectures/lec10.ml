type expr = 
| Constant of int
| Negate of expr
| Add of expr * expr
| Mult of expr * expr

let a = Negate(Add(Constant(1), Constant(2)))

let rec eval ((e:expr)): int = 
  match e with
  | Constant(n) -> n
  | Negate(e') -> -(eval e')
  | Add(e1, e2) -> eval e1 + eval e2
  | Mult(e1,e2) -> eval e1 * eval e2

let max ((n1: int), (n2: int)): int = 
  if n1 > n2 then n1 else n2

(** return the largest constant in the tree *)
let rec max_const ((e: expr)): int = 
  match e with
  | Constant(n) -> n
  | Negate(e') -> max_const e'
  | Add(e1, e2) -> max(max_const(e1), max_const(e2))
  | Mult(e1, e2) -> max(max_const(e1), max_const(e2))

let rec number_of_add_ops ((e: expr)): int = 
  match e with
  | Constant(n) -> 0
  | Negate(e') -> number_of_add_ops(e')
  | Add(e1, e2) -> 1 + number_of_add_ops(e1) + number_of_add_ops(e2)
  | Mult(e1, e2) -> number_of_add_ops(e1) + number_of_add_ops(e2)

type 'a myoption = 
| MyNone
| MySome of 'a

let x: int myoption = MySome(3)
let x2 = MySome(3)
let x4 = MySome("hello")
let y: int myoption = MyNone
let y2 = MyNone

let f ((o: int myoption)): int = 
  match o with
  | MyNone -> 17
  | MySome(n) -> n + 4

(* built in lists are almost like:

type 'a list =
| []
| :: of 'a * 'a list

*)

let rec sum ((l: int list)): int = 
  match l with
  | [] -> 0
  | x::xs -> x + sum(xs)

let t: int * string * bool = (1, "hello", false)

let s =
  match t with
  | (n, s, b) -> Int.to_string(n) ^ s ^ Bool.to_string b

let s2 = 
  let (n, s, b) = t in
  Int.to_string(n) ^ s ^ Bool.to_string b

(*
let this_will_raise_exception =
  let x::xs = [] in
  x
  *)

let this_will_work = 
  let x::xs = [1; 2; 3] in
  x

let rec sum_int_option_list ((l: int option list)) = 
  match l with
  | [] -> 0
  | x::xs -> 
    match x with
    | None -> sum_int_option_list(xs)
    | Some(n) -> n + sum_int_option_list(xs)

let rec sum_int_option_list2 ((l: int option list)) = 
  match l with
  | [] -> 0
  | None::xs -> sum_int_option_list2(xs)
  | Some(n)::xs -> n + sum_int_option_list2(xs)

let rec sum_int_option_pair_list3 ((l: ((int * int) option) list)): int = 
  match l with
  | [] -> 0
  | None::xs -> sum_int_option_pair_list3(xs)
  | Some(n1, n2)::xs -> n1 + n2 + sum_int_option_pair_list3(xs)

let rec zip3 ((l1: 'a list), (l2: 'b list), (l3: 'c list)): ('a * 'b * 'c) list = 
  match l1 with
  | [] -> []
  | x::xs ->
    match l2 with
    | [] -> []
    | y::ys ->
      match l3 with
      | [] -> []
      | z::zs -> (x, y, z) :: zip3(xs, ys, zs)

let l1 = [1; 2; 3]
let l2 = ["hello"; ""; "world"]
let l3 = [true; false; false; true]


let rec zip3((l1: 'a list), (l2: 'b list), (l3: 'c list)): ('a * 'b * 'c) list =
  match (l1, l2 ,l3) with
  | (x::xs, y::ys, z::zs) -> (x, y, z) :: zip3(xs, ys, zs)
  | _ -> []

(* what about throwing an error if they have different lengths *)
let rec zip3((l1: 'a list), (l2: 'b list), (l3: 'c list)): ('a * 'b * 'c) list =
  match (l1, l2, l3) with
  | ([], [], []) -> []
  | (x::xs, y::ys, z::zs) -> (x,y,z) :: zip3(xs, ys, zs)
  | _ -> failwith "zip3: lists of different length"
  