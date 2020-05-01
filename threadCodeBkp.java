            // int splitNumber = 10;

            // // splitting states arraylist, previousValueFunction, and currentValueFuntion
            // // for
            // // threads
            // CarState[] arr = new CarState[states.size()];
            // arr = states.toArray(arr);
            // int chunk = arr.length / splitNumber;
            // CarState[][] splitState;
            // double[][] splitCurrValue;
            // double[][] splitPrevValue;
            // Boolean divided;
            // if (arr.length % splitNumber == 0) {
            //     divided = Boolean.TRUE;
            //     splitState = new CarState[splitNumber][chunk];
            //     splitCurrValue = new double[splitNumber][chunk];
            //     splitPrevValue = new double[splitNumber][chunk];
            // } else {
            //     divided = Boolean.FALSE;
            //     splitState = new CarState[splitNumber + 1][chunk];
            //     splitCurrValue = new double[splitNumber + 1][chunk];
            //     splitPrevValue = new double[splitNumber + 1][chunk];
            // }

            // int j = 0;
            // for (int i = 0; i < arr.length; i += chunk) {
            //     splitState[j] = Arrays.copyOfRange(arr, i, Math.min(arr.length, i + chunk));
            //     splitCurrValue[j] = Arrays.copyOfRange(currentValueFunction, i,
            //             Math.min(currentValueFunction.length, i + chunk));
            //     splitPrevValue[j] = Arrays.copyOfRange(previousValueFunction, i,
            //             Math.min(previousValueFunction.length, i + chunk));
            //     j++;
            // }

            // // splitting qtableValues and stateActionPairs for threads
            // // StateActionPair saArr = new StateActionPair[stateActionPairs.size()];
            // // saArr = stateActionPairs.toArray(saArr);
            // // double[][] splitQTable;
            // // chunk = qtableValues.length / splitNumber;
            // // if (qtableValues.length % splitNumber == 0) {
            // //     divided = true;
            // //     splitQTable = new double[splitNumber][chunk];
            // // } else {
            // //     splitQTable = new double[splitNumber + 1][chunk];
            // // }
            // // j = 0;
            // // for (int i = 0; i < qtableValues.length; i++) {
            // //     splitQTable[j] = Arrays.copyOfRange(qtableValues, i, Math.min(qtableValues.length, i + chunk));
            // //     j++;
            // // }

            // // splitting

            // int threadCount;
            // if (divided) {
            //     threadCount = splitNumber;
            // } else {
            //     threadCount = splitNumber + 1;
            // }
            // ArrayList<ValueIterationThread> threads = new ArrayList<>();
            // for (int i = 0; i < threadCount; i++) {
            //     ValueIterationThread thread = new ValueIterationThread(course, epsilon, discountFactor, badCrash,
            //             splitCurrValue[i], splitCurrValue[i], qtableValues, new ArrayList<CarState>(Arrays.asList(splitState[i])), actions, stateActionPairs);
            //     threads.add(thread);
            //     thread.start();
            // }
            // int currentValueFunctionIndex = 0;
            // for (ValueIterationThread valueIterationThread : threads) {
            //     try {
            //         valueIterationThread.join();
            //     } catch (InterruptedException e) {
            //         // TODO Auto-generated catch block
            //         System.err.println("Thread Execption");
            //         e.printStackTrace();
            //     }
            //     int i;
            //     for (i=currentValueFunctionIndex; i<valueIterationThread.getValueFunctionChunck().length+currentValueFunctionIndex; i++) {{
                    
            //     }
            //     currentValueFunctionIndex=i;
                    
            //     }
            // }