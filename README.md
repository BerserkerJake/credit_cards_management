An Example of how updateBalanceFromPayload Works
==========================
* If today is 4/12, a credit card's balanceHistory is [{date: 4/12, balance: 110}, {date: 4/10, balance: 100}],
* and we have 3 new transactions to update {date: 4/9, amount: 90}, {date: 4/11, amount: 110} and {date: 4/12, amount: 110},
* then the updates look like this:
-------------------------------------------
* ___First payload:___
 
     *   balanceDate = 4/9
     *   balanceAmount = 90
     *   firstDate = 4/9
     *   lastDate = 4/12
     *   date = 4/9:  (Balance Date!!)
     *     newHistory.setDate(4/9)
     *     previousBalance = 0
     *     diffBalance = 90 - 0 = 90
     *     newHistory.setBalance(0 + 90 = 90)
     *     balanceHistory: [{date: 4/12, balance: 110}, {date: 4/10, balance: 100}, {date: 4/9, balance: 90}]
     *   date = 4/10:
     *     newHistory = balanceHistory.get(4/10)
     *     previousBalance = newHistory.getBalance() = 100
     *     diffBalance = 0
     *     newHistory.setBalance(100 + 0 = 100)
     *     balanceHistory: [{date: 4/12, balance: 110}, {date: 4/10, balance: 100}, {date: 4/9, balance: 90}]
     *   date = 4/12:
     *     newHistory = balanceHistory.get(4/12)
     *     previousBalance = newHistory.getBalance() = 110
     *     diffBalance = 0
     *     newHistory.setBalance(110 + 0 = 110)
     *     balanceHistory: [{date: 4/12, balance: 110}, {date: 4/10, balance: 100}, {date: 4/9, balance: 90}]
-------------------------------------------
* ___Second payload:___
    
     *   balanceDate = 4/11
     *   balanceAmount = 110
     *   firstDate = 4/9
     *   lastDate = 4/12
     *   date = 4/9: 
     *     newHistory = balanceHistory.get(4/9)
     *     previousBalance = newHistory.getBalance() = 90
     *     diffBalance = 0
     *     newHistory.setBalance(90 + 0 = 90)
     *     balanceHistory: [{date: 4/12, balance: 110}, {date: 4/10, balance: 100}, {date: 4/9, balance: 90}]
     *   date = 4/10:
     *     newHistory = balanceHistory.get(4/10)
     *     previousBalance = newHistory.getBalance() = 100
     *     diffBalance = 0
     *     newHistory.setBalance(100 + 0 = 100)
     *     balanceHistory: [{date: 4/12, balance: 110}, {date: 4/10, balance: 100}, {date: 4/9, balance: 90}]
     *   date = 4/11:  (Balance Date!!)
     *     newHistory.setDate(4/11)
     *     previousBalance = 100
     *     diffBalance = 110 - 100 = 10
     *     newHistory.setBalance(100 + 10 = 110)
     *     balanceHistory: [{date: 4/12, balance: 110}, {date: 4/11, balance: 110}, {date: 4/10, balance: 100}, {date: 4/9, balance: 90}]
     *   date = 4/12:
     *     newHistory = balanceHistory.get(4/12)
     *     previousBalance = newHistory.getBalance() = 110
     *     diffBalance = 10
     *     newHistory.setBalance(110 + 10 = 120)
     *     balanceHistory: [{date: 4/12, balance: 120}, {date: 4/11, balance: 110}, {date: 4/10, balance: 100}, {date: 4/9, balance: 90}]
-------------------------------------------
* ___Third payload:___
    
     *   balanceDate = 4/12
     *   balanceAmount = 110
     *   firstDate = 4/9
     *   lastDate = 4/12
     *   date = 4/9:
     *     newHistory = balanceHistory.get(4/9)
     *     previousBalance = newHistory.getBalance() = 90
     *     diffBalance = 0
     *     newHistory.setBalance(90 + 0 = 90)
     *     balanceHistory: [{date: 4/12, balance: 120}, {date: 4/11, balance: 110}, {date: 4/10, balance: 100}, {date: 4/9, balance: 90}]
     *   date = 4/10:
     *     newHistory = balanceHistory.get(4/10)
     *     previousBalance = newHistory.getBalance() = 100
     *     diffBalance = 0
     *     newHistory.setBalance(100 + 0 = 100)
     *     balanceHistory: [{date: 4/12, balance: 120}, {date: 4/11, balance: 110}, {date: 4/10, balance: 100}, {date: 4/9, balance: 90}]
     *   date = 4/11:
     *     newHistory = balanceHistory.get(4/11)
     *     previousBalance = newHistory.getBalance() = 110
     *     diffBalance = 0
     *     newHistory.setBalance(110 + 0 = 110)
     *     balanceHistory: [{date: 4/12, balance: 120}, {date: 4/11, balance: 110}, {date: 4/10, balance: 100}, {date: 4/9, balance: 90}]
     *   date = 4/12:  (Balance Date!!)
     *     newHistory = balanceHistory.get(4/12)
     *     previousBalance = newHistory.getBalance() = 120
     *     diffBalance = 110 - 120 = -10
     *     newHistory.setBalance(120 - 10 = 110)
     *     balanceHistory: [{date: 4/12, balance: 110}, {date: 4/11, balance: 110}, {date: 4/10, balance: 100}, {date: 4/9, balance: 90}]
-------------------------------------------
* The final balanceHistory after the updates:
* [{date: 4/12, balance: 110}, {date: 4/11, balance: 110}, {date: 4/10, balance: 100}, {date: 4/9, balance: 90}]
