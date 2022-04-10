(* CSE 341, Homework 2, Provided Code *)

(* Use these functions to extract parts of a date *)
let fst3 (x,_,_) = x (* gets the first element of a triple *)
let snd3 (_,x,_) = x (* gets the second element of a triple *)
let thd3 (_,_,x) = x (* gets the third element of a triple *)

(* 1 *)
let is_older ((date1 : int * int * int), (date2 : int * int * int)) =
  if thd3(date1) != thd3(date2)
  then thd3(date1) < thd3(date2)
  else if snd3(date1) != snd3(date2)
  then snd3(date1) < snd3(date2)
  else fst3(date1) < fst3(date2)
 
(* 2 *)
let rec number_in_month ((dates : (int * int * int) list), (month : int)) =
  if dates = []
  then 0
  else if snd3(List.hd(dates)) = month
  then 1 + number_in_month(List.tl(dates), month)
  else number_in_month(List.tl(dates), month)

(* 3 *)
let rec number_in_months ((dates : (int * int * int) list), (months : int list)) =
  if months = []
  then 0
  else number_in_month(dates, List.hd(months)) + number_in_months(dates, List.tl(months)) 

(* 4 *)
let rec dates_in_month ((dates : (int * int * int) list), (month : int)) =
  if dates = []
  then []
  else if snd3(List.hd(dates)) = month
  then List.hd(dates) :: dates_in_month(List.tl(dates), month)
  else dates_in_month(List.tl(dates), month)

(* 5 *)
let rec dates_in_months ((dates : (int * int * int) list), (months : int list)) =
  if months = []
  then []
  else dates_in_month(dates, List.hd(months))@dates_in_months(dates, List.tl(months))

(* 6 *)
let rec get_nth ((strings : string list), (n : int)) = 
  if n = 1
  then List.hd(strings)
  else get_nth(List.tl(strings), n - 1)

(* 7 *)
let string_of_date ((day : int), (month : int), (year : int)) = 
  let months = ["January"; "February"; "March"; "April"; "May"; "June"; "July"; "August"; "September"; "October"; "November"; "December"] in
  get_nth(months, month) ^ "-" ^ string_of_int(day) ^ "-" ^ string_of_int(year)

(* 8 *)
let rec number_before_reaching_sum ((sum : int), (numbers : int list)) = 
  if sum < 0
  then -1
  else 1 + number_before_reaching_sum(sum - List.hd(numbers), List.tl(numbers))

(* 9 *)
let what_month ((n : int)) = 
  let count = [31; 28; 31; 30; 31; 30; 31; 31; 30; 31; 30; 31] in
  1 + number_before_reaching_sum(n - 1, count)

(* 10 *)
let rec month_range ((day1 : int), (day2 : int)) = 
  if day1 > day2
  then []
  else what_month(day1) :: month_range(day1 + 1, day2)

(* 11 *)
let rec oldest (dates : (int * int * int) list): (int * int * int) option = 
  if dates = []
  then None
  else if List.tl dates = []
  then Some(List.hd dates)
  else
    let oldest_of_tail = oldest(List.tl dates) in
    if is_older(List.hd dates, Option.get(oldest_of_tail))
    then Some(List.hd dates)
    else oldest_of_tail

let rec oldest2 (dates : (int * int * int) list) =
  if dates = []
  then None
  else if List.tl(dates) = []
  then Some(List.hd(dates))
  else if is_older(List.hd(dates), List.hd(List.tl(dates)))
  then oldest2(List.hd(dates) :: List.tl(List.tl(dates)))
  else oldest2(List.tl(dates))

(* 12 *)
let cumulative_sum ((numbers : int list)) =
  let rec cumulative_helper ((sum : int), (nums : int list)) = 
    if nums = []
    then []
    else (sum + List.hd(nums)) :: cumulative_helper(sum + List.hd(nums), List.tl(nums)) in
  cumulative_helper(0, numbers)