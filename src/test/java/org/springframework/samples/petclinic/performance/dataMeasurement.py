"""#Calculate Add Pet Performance Time as Pet Increases"""

import matplotlib.pyplot as plt
import numpy as np


x = np.array([200,500,800,1000,2000,5000,8000])
y = np.array([12023790.5, 6211230.8,4845638.75,4162784,3999156.75,3966693.4,3764218.225])
line1 = plt.plot(x, y, marker='o')

# naming the x axis
plt.xlabel('Number of Pet added in Database')
# naming the y axis
plt.ylabel('Average Runtime to Add one Pet (ns)')
# giving a title to my graph
plt.title('Runtime Performance of Adding one Pet')


plt.show()

"""#Calculate Modify Pet Performance Time as Pet Increases"""

import matplotlib.pyplot as plt
import numpy as np


x = np.array([200,500,800,1000,2000,5000,8000])
y = np.array([3861548,3051684.6,2665407.75,2268410.1,2175097.45,2128754.56,2074975.775])
line1 = plt.plot(x, y, marker='o')


# naming the x axis
plt.xlabel('Number of Pet added in Database')
# naming the y axis
plt.ylabel('Average Runtime to Modify one Pet (ns)')

# giving a title to my graph
plt.title('Runtime Performance of Modifying one Pet')

plt.show()

"""# Calculate Add Owner Performance Time as Owner Increases"""

import matplotlib.pyplot as plt
import numpy as np


x = np.array([200,500,800,1000,2000,5000,8000])
y = np.array([4732652.5,2795302.4,2252910.5,1961307.1,1699187.95,1486512.46,1367733.75])
line1 = plt.plot(x, y, marker='o')

# naming the x axis
plt.xlabel('Number of Owners added in Database')
# naming the y axis
plt.ylabel('Average Runtime to Add one Owner (ns)')

# giving a title to my graph
plt.title('Runtime Performance of Adding one Owner')

plt.show()

"""# Calculate Modify Owner Performance Time as Owner Increases"""

import matplotlib.pyplot as plt
import numpy as np


x = np.array([200,500,800,1000,2000,5000,8000])
y = np.array([4547785.5,3102952,2515559.88,2066201.2,1768124.55,1536997.84,1399305.71])
line1 = plt.plot(x, y, marker='o')


# naming the x axis
plt.xlabel('Number of Owners added in Database')
# naming the y axis
plt.ylabel('Average Runtime to Modify one Owner (ns)')

# giving a title to my graph
plt.title('Runtime Performance of Modifying one Owner')

plt.show()

