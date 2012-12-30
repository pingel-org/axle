
# neuralnet.r

x = c(x0, x1, x2, x3)
z1 = Theta1 %*% x
a1 = g(z1)
z2 = Theta1 %*% a1
a2 = g(z2) # a0 bias unit = 1
z3 = Theta2 %*% a2
a3 = g(z3)
# h(x) = a3


# cost function J(Theta):

cost <- function(Theta) {

 - (1/m)
   * ( Sum(i=1..m) (
         Sum(k=1..K) (
           yi_k * log( h(theta, xi)_k ) +
           (1-yi_k) * log(1 - h(theta, xi)_k)
         )
       ) 
     )

 + (lambda / 2 * m) 
   * ( Sum(l=1..L-1) (
         Sum(i=1..s_l) (
           Sum(j=1..(s_l+1) ) (
             (Thetal_j)^2
           )
         )
       )
     )

}

# "error" of coast for al_j (unit j in layer l)

delta <- function(l, j) {
  # unit j
  # layer l
  Sum(n=1..N) (  # N units in layer l+1
    Thetal_(n, j) * delta(l+1, n)
  )
}

gradApprox <- function(theta) {
  for( i <- 1 to n) {
    thetaPlus = theta;
    thetaPlus(i) = thetaPlus(i) + EPSILON
    thetaMinus = theta;
    thetaMinus(i) = thetaMinus(i) + EPSILON
    gradApprox(i) = (J(thetaPlus) - J(thetaMinus)) / (2 * EPSILON)
  }
}

# gradient checking:
#  compare gradApprox to DVec to help QA the implementation



# Overflow of implementation plan:

# 1 random initialization:

Theta1 = rand(10,11) * (2 * INIT_EPSILON )  - INIT_EPSILON;

# 2 implement FP to get h_theta(xi) for any xi

# 3 implement cost function J(Theta)

# 4 implement BP to compute partial derivatives
#    d/dTheta^(l)_jk J(Theta)

for i = 1: m {
  perform fp and bp using example (xi,yi)
  get activations al and delta derms deltal for l = 2 .. L
  Deltal := Deltal + delta^(l+1) * (a^(l))t
}
compute d/dTheta^(?)_jk * J(Theta)

# 5 using gradient checking to compare
#      BP vs numerical estimation 
#      of gradient J(Theta)
#   Then disable gradient checking code
#

# 6 Using gradient descent or advanced optimization method with BP to try to 
# minimize J(Theta) as a function of parameters Theta


