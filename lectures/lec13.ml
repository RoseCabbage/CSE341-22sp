type expr = 
  | Constant of int
  | Plus of expr * expr
  | Minus of expr * expr
  | Variable of string

type association_list = (string * expr) list
type dynamic_environment = association_list

let rec lookup ((assoc_list : association_list), (key : string)): expr = 
  match assoc_list with
  | [] -> failwith "lookup failed to find key"
  | (key', v) :: list ->
    if key' = key
    then v
    else lookup (list, key)

let rec eval(dynenv, e) = 
  match e with
  | Constant(n) -> n
  | Plus(e1, e2) -> eval (dynenv, e1) + eval (dynenv, e2)
  | Minus(e1, e2) -> eval (dynenv, e1) - eval (dynenv, e2)
  | Variable(x) ->
    eval(dynenv, lookup (dynenv, x))