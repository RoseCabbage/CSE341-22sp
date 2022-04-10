let x = 3

let rec range ((lo: int), (hi: int)) = 
  if lo >= hi
  then []
  else
    let next_lo = lo + 1 in
    lo :: range(next_lo, hi)

let nats_upto1 ((n: int)) = range(0, n)

let nats_upto2 ((n: int)) = 
  let rec range ((lo: int), (hi: int)) = 
    if lo >= hi
    then []
    else lo :: range(lo + 1, hi)
  in range(0, n)

let nats_upto3 ((n: int)) =
  let rec loop ((lo: int)) = 
    if lo >= n
    then []
    else lo :: loop(lo + 1)
  in loop(0)

let rec bad_max ((xs: int list)) = 
  if xs = []
  then 0
  else if List.tl xs = []
  then List.hd xs
  else if List.hd xs > bad_max(List.tl xs)
  then List.hd xs
  else bad_max(List.tl xs)

let rec better_max ((xs: int list)) =
  if xs = []
  then 0
  else if List.tl xs = []
  then List.hd xs
  else 
    let max_of_tail = better_max(List.tl xs) in
    if List.hd xs > max_of_tail
    then List.hd xs
    else
      max_of_tail

let rec good_max ((xs: int list)): int option = 
  if xs = []
  then None
  else if List.tl xs = []
  then Some(List.hd xs)
  else
    let max_of_tail = good_max(List.tl xs) in
    if List.hd xs > Option.get(max_of_tail)
    then Some(List.hd xs)
    else max_of_tail