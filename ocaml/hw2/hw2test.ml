open Hw2

(* 1 *)
let%test _ = is_older((15, 1, 2021), (1, 4, 2022))
let%test _ = not (is_older((1, 2, 3), (3, 2, 1)))
let%test _ = not (is_older((1, 2, 3), (1, 2, 3)))
let%test _ = is_older((15, 1, 2021), (16, 1, 2021))
let%test _ = is_older((15, 1, 2021), (15, 3, 2021))

(* 2 *)
let%test _ = number_in_month([(15, 1, 2021); (1, 4, 2022)], 1) = 1
let%test _ = number_in_month([(15, 1, 2021); (1, 4, 2022)], 2) = 0
let%test _ = number_in_month([(12, 1, 2021); (1, 12, 2022); (1, 2, 12)], 12) = 1

(* 3 *)
let%test _ = number_in_months([(15, 1, 2021); (1, 4, 2022)], [1; 4]) = 2
let%test _ = number_in_months([(15, 1, 2021); (1, 4, 2022)], [2; 3]) = 0
let%test _ = number_in_months([(15, 1, 2021); (1, 4, 2022)], [1; 5]) = 1

(* 4 *)
let%test _ = dates_in_month([(15, 1, 2021); (1, 4, 2022)], 1) = [(15, 1, 2021)]
let%test _ = dates_in_month([(15, 1, 2021); (1, 4, 2022)], 2) = []
let%test _ = dates_in_month([(15, 1, 2021); (1, 1, 2022)], 1) = [(15, 1, 2021); (1, 1, 2022)]

(* 5 *)
let%test _ = dates_in_months([(15, 1, 2021); (1, 4, 2022)], [1; 2]) = [(15, 1, 2021)]
let%test _ = dates_in_months([(15, 1, 2021); (1, 4, 2022)], [2; 3]) = []
let%test _ = dates_in_months([(15, 1, 2021); (1, 4, 2022)], [1; 4]) = [(15, 1, 2021); (1, 4, 2022)]

(* 6 *)
let%test _ = get_nth(["a"; "bb"; "ccc"; "dddd"], 1) = "a"
let%test _ = get_nth(["a"; "bb"; "ccc"; "dddd"], 2) = "bb"
let%test _ = get_nth(["a"; "bb"; "ccc"; "dddd"], 3) = "ccc"
let%test _ = get_nth(["a"; "bb"; "ccc"; "dddd"], 4) = "dddd"

(* 7 *)
let%test _ = string_of_date((10, 9, 2015)) = "September-10-2015"
let%test _ = string_of_date((19, 1, 2022)) = "January-19-2022"
let%test _ = string_of_date((18, 12, 2017)) = "December-18-2017"

(* 8 *)
let%test _ = number_before_reaching_sum(0, [1; 2; 3; 4]) = 0
let%test _ = number_before_reaching_sum(1, [1; 2; 3; 4]) = 1
let%test _ = number_before_reaching_sum(2, [1; 2; 3; 4]) = 1
let%test _ = number_before_reaching_sum(9, [1; 2; 3; 4]) = 3

(* 9 *)
let%test _ = what_month(1) = 1
let%test _ = what_month(31) = 1
let%test _ = what_month(365) = 12
let%test _ = what_month(227) = 8

(* 10 *)
let%test _ = month_range(1, 3) = [1; 1; 1]
let%test _ = month_range(1, 1) = [1]
let%test _ = month_range(20, 3) = []
let%test _ = month_range(31, 32) = [1; 2]

(* 11 *)
let%test _ = oldest([]) = None
let%test _ = oldest([(1, 1, 2022); (1, 1, 2021)]) = Some((1, 1, 2021))
let%test _ = oldest([(1, 1, 2022); (1, 1, 2021); (1, 1, 2021); (1, 1, 2023)]) = Some((1, 1, 2021))

(* 12 *)
let%test _ = cumulative_sum([12; 27; 13]) = [12; 39; 52]
let%test _ = cumulative_sum([]) = []
let%test _ = cumulative_sum([12; -27; 13]) = [12; -15; -2]