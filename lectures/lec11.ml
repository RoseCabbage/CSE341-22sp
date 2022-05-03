let rec sum (l: int list): int = 
  match l with
  | [] -> 0
  | x::xs -> x + sum(xs)

let rec countdown (n: int): int list = 
  if n = 0
  then []
  else n :: countdown(n - 1)

let sum2 (l: int list): int =
  let rec loop ((l: int list), (acc: int)): int = 
    match l with
    | [] -> acc
    | x::xs -> loop(xs, acc + x)
  in
  loop(l, 0)

let countdown2 (n: int): int list = 
  let rec countdown2_helper ((n: int), (acc: int list)) = 
    if n = 0
    then List.rev acc
    else countdown2_helper(n - 1, n :: acc)
  in
  countdown2_helper(n, [])