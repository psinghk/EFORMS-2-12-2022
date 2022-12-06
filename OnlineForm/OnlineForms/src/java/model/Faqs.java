/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author NIC
 */
public class Faqs {
    int Id;
    String e_cate,e_question,e_answer;

    public int getId() {
        return Id;
    }

    public void setId(int Id) {
        this.Id = Id;
    }

    public String getE_cate() {
        return e_cate;
    }

    public void setE_cate(String e_cate) {
        this.e_cate = e_cate;
    }

    public String getE_question() {
        return e_question;
    }

    public void setE_question(String e_question) {
        this.e_question = e_question;
    }

    public String getE_answer() {
        return e_answer;
    }

    public void setE_answer(String e_answer) {
        this.e_answer = e_answer;
    }

}
