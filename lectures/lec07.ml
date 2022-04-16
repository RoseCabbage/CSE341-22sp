type lava_lamp = 
  { height       : float
  ; color_liquid : string
  ; color_lava   : string
  }

let my_lamp1 = 
  { height       = 13.5 +. 1.0
  ; color_liquid = "bl" ^ "ue"
  ; color_lava   = "" ^ "green" ^ ""
  }

let my_lamp2 = { height = 14.4; color_liquid = my_lamp1.color_liquid; color_lava = "x"}

let a = my_lamp1.height
let b = my_lamp1.color_liquid
let c = my_lamp1.color_lava

let concat_liquid_colors ((lamp1 : lava_lamp), (lamp2 : lava_lamp)) = 
  lamp1.color_liquid ^ " " ^ lamp2.color_liquid

let epsilon = 0.0001
let same_height ((lamp1 : lava_lamp), (lamp2 : lava_lamp)) = 
  Float.abs (lamp1.height -. lamp2.height) < epsilon