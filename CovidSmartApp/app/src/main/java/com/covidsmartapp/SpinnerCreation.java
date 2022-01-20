package com.covidsmartapp;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

public class SpinnerCreation {
    Context context;
    String [] countries = {"Afghanistan", "Albania", "Algeria", "Andorra", "Angola", "Anguilla", "Antigua and Barbuda",
            "Argentina", "Armenia", "Aruba", "Australia", "Austria", "Azerbaijan", "Bahamas", "Bahrain", "Bangladesh",
            "Barbados", "Belarus", "Belgium", "Belize", "Benin", "Bermuda", "Bhutan", "Bolivia", "Bosnia", "Botswana",
            "Brazil", "British Virgin Islands", "Brunei", "Bulgaria", "Burkina Faso", "Burundi", "Cabo Verde", "Cambodia",
            "Cameroon", "Canada", "Caribbean Netherlands", "Cayman Islands", "Central African Republic", "Chad",
            "Channel Islands", "Chile", "China", "Colombia", "Comoros", "Congo", "Costa Rica", "Croatia", "Cuba",
            "Curaçao", "Cyprus", "Czechia", "Côte d'Ivoire", "DRC", "Denmark", "Diamond Princess", "Djibouti", "Dominica",
            "Dominican Republic", "Ecuador", "Egypt", "El Salvador", "Equatorial Guinea", "Eritrea", "Estonia", "Ethiopia",
            "Falkland Islands (Malvinas)", "Faroe Islands", "Fiji", "Finland", "France", "French Guiana", "French Polynesia",
            "Gabon", "Gambia", "Georgia", "Germany", "Ghana", "Gibraltar", "Greece", "Greenland", "Grenada", "Guadeloupe",
            "Guatemala", "Guinea", "Guinea-Bissau", "Guyana", "Haiti", "Holy See (Vatican City State)", "Honduras", "Hong Kong",
            "Hungary", "Iceland", "India", "Indonesia", "Iran", "Iraq", "Ireland", "Isle of Man", "Israel", "Italy", "Jamaica",
            "Japan", "Jordan", "Kazakhstan", "Kenya", "Kuwait", "Kyrgyzstan", "Lao People's Democratic Republic", "Latvia",
            "Lebanon", "Lesotho", "Liberia", "Libyan Arab Jamahiriya", "Liechtenstein", "Lithuania", "Luxembourg", "MS Zaandam",
            "Macao", "Macedonia", "Madagascar", "Malawi", "Malaysia", "Maldives", "Mali", "Malta", "Marshall Islands", "Martinique",
            "Mauritania", "Mauritius", "Mayotte", "Mexico", "Micronesia", "Moldova", "Monaco", "Mongolia", "Montenegro",
            "Montserrat", "Morocco", "Mozambique", "Myanmar", "Namibia", "Nepal", "Netherlands", "New Caledonia", "New Zealand",
            "Nicaragua", "Niger", "Nigeria", "Norway", "Oman", "Pakistan", "Palau", "Palestine", "Panama", "Papua New Guinea",
            "Paraguay", "Peru", "Philippines", "Poland", "Portugal", "Qatar", "Romania", "Russia", "Rwanda", "Réunion",
            "S. Korea", "Saint Helena", "Saint Kitts and Nevis", "Saint Lucia", "Saint Martin", "Saint Pierre Miquelon",
            "Saint Vincent and the Grenadines", "Samoa", "San Marino", "Sao Tome and Principe", "Saudi Arabia", "Senegal",
            "Serbia", "Seychelles", "Sierra Leone", "Singapore", "Sint Maarten", "Slovakia", "Slovenia", "Solomon Islands",
            "Somalia", "South Africa", "South Sudan", "Spain", "Sri Lanka", "St. Barth", "Sudan", "Suriname", "Swaziland",
            "Sweden", "Switzerland", "Syrian Arab Republic", "Taiwan", "Tajikistan", "Tanzania", "Thailand", "Timor-Leste",
            "Togo", "Tonga", "Trinidad and Tobago", "Tunisia", "Turkey", "Turks and Caicos Islands", "UAE", "UK", "USA",
            "Uganda", "Ukraine", "Uruguay", "Uzbekistan", "Vanuatu", "Venezuela", "Vietnam", "Wallis and Futuna",
            "Western Sahara", "Yemen", "Zambia", "Zimbabwe"};

    public SpinnerCreation(Context context) {
        this.context = context;
    }

    public SearchableSpinner createSpinner (SearchableSpinner spinner) {
        ArrayAdapter<String> countryA = new ArrayAdapter<>(this.context, android.R.layout.simple_spinner_item, countries);
        countryA.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(countryA);

        spinner.setTitle("Select Country");
        spinner.setPositiveButton("OK");

        return spinner;
    }
}
