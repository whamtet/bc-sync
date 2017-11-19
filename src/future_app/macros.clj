(ns future-app.macros)

(defmacro promise-> [statement & chain]
  `(-> ~statement
       ~@(for [statement chain]
           (if (vector? statement)
             `(.then (fn [~'then] ~(first statement)) (fn [~'then] ~(second statement)))
             `(.then (fn [~'then] ~statement))))))
