
normalEquation <- function(X, y) {
   (t(X) %*% X)^{-1} %*% t(X) %*% y
}

scaleX <- function(X, y) {
   xRange = apply(X, 2, max) - apply(X, 2, min)
   xRange[1] = 1
   Xfloored = X - matrix(rep(apply(X, 2, min), nrow(X)), nrow(X), ncol(X), byrow=TRUE)
   Xscaled = t(diag(xRange^{-1}) %*% t(Xfloored))
   Xscaled[,1] = 1
   Xscaled   
}

scaleY <- function(y) {
   yFloored = y - t(t(rep(apply(y, 2, min), nrow(X))))
   yScaled = yFloored / apply(yFloored, 2, max)
   yScaled
}

h <- function(xi, theta) {
   xi %*% theta
}

gradientDescentUpdate <- function(X, y, theta) {
   result = matrix(rep(0), ncol(X), 1, byrow=TRUE)
   for (i in 1:nrow(X)) {
      ri = X[i,] %*% ( h(X[i,], theta) - y[i,1] )
      result = result + ri
   }
	result = result / nrow(X)
	result
}

gradientDescent <- function(X, y, theta, alpha, iterations) {
   for (k in 1:iterations) {
		# print(k)
		dTheta = gradientDescentUpdate(X, y, theta)
		# print(dTheta)
		theta = theta - (alpha * dTheta)
		# print(theta)
	}
	theta
}

normalEquation <- function(X, y) { 
   (t(X) %*% X)^{-1} %*% t(X) %*% y
}

y = matrix(c(460, 232, 315, 178), 4, 1, byrow=TRUE)

X = matrix( c(
   1, 2104, 5, 1, 45,
   1, 1416, 3, 2, 40,
   1, 1534, 3, 2, 30,
   1,  852, 2, 1, 36),
   4,
   5,
   byrow=TRUE)

Xs = scaleX(X)
ys = scaleY(y)

# gradientDescent(Xs, ys, t(t(rep(1, ncol(Xs)))), 0.1, 100)

# TODO: an h that incororates the scaling that was applied in scaleX and scaleY

# source("/Users/pingel/github-clones/pingel.org/ai/regression.r")

