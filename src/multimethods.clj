(ns multimethods)

(defmulti area :shape)
; keywords can be used as fn

(defmethod area :circle [{:keys [radius]}]
  (* Math/PI radius radius))

(defmethod area :rectangle [{:keys [width height]}]
  (* width height))

(comment
  (area {:shape :circle :radius 10})
  (area {:shape :rectangle :width 10 :height 10})
  )


(defmulti area2 (fn [shape & data] (:shape shape)))

(defmethod area2 :circle [shape radius]
  (prn "shape param" shape) ; have the original params not the result of dispatch fn
  (* Math/PI radius radius))

(defmethod area2 :rectangle [shape width height]
  (* width height))

(comment
  (area2 {:shape :circle} 10)
  (area2 {:shape :rectangle} 10  10)
  )