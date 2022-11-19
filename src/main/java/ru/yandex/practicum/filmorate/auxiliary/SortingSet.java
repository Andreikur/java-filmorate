package ru.yandex.practicum.filmorate.auxiliary;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class SortingSet {

    public static Set<Integer> SortingSet(Set<Integer> listOfFriends){
        List<Integer> myList = new  ArrayList<>();
        for (int id: listOfFriends){
            if(myList.isEmpty()){
                myList.add(id);
            } else {
                for (int i = 0;  i< myList.size(); i++){
                    if(myList.get(i) < id){
                        myList.add(i, id);
                        break;
                    }
                    myList.add(id);
                }
            }
        }
        listOfFriends.addAll(myList);
        return listOfFriends;
    }
}
