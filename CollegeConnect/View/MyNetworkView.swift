//
//  MyNetworkView.swift
//  CollegeConnect
//
//  Created by Deep Awasthi on 22/5/24.
//

import SwiftUI

var networkData: [NetworkModel] = [
    .init(id: 0, name: "Marry", position: "SDE at Paytm", mutual: 34, image: "00"),
    .init(id: 1, name: "Peter", position: "Assistant Manager", mutual: 45, image: "01"),
    .init(id: 2, name: "Jenny", position: "SDE at Ginger ", mutual: 67, image: "02"),
    .init(id: 3, name: "Sara", position: "open to work", mutual: 103, image: "03"),
    .init(id: 4, name: "Kia", position: "GET at HCL", mutual: 12, image: "04"),
    .init(id: 5, name: "Shristi", position: "Student", mutual: 78, image: "05"),
    .init(id: 6, name: "Rachel", position: "intern at iNeuron", mutual: 90, image: "00"),
    .init(id: 7, name: "John", position: "HR at Intel", mutual: 34, image: "01"),
    .init(id: 8, name: "Tiya", position: "purchase Engineer", mutual: 50, image: "02"),
    .init(id: 9, name: "Pheobe", position: "Product Manager", mutual: 86, image: "03"),
    .init(id: 10, name: "Monica", position: "Data Analyst", mutual: 55, image: "04"),
    .init(id: 11, name: "Joe", position: "Software Development intern", mutual: 42, image: "05"),
    .init(id: 12, name: "Charel", position: "SDE-II ", mutual: 30, image: "00"),
    .init(id: 13, name: "Chandler", position: "Mobile Developer", mutual: 71, image: "01"),
    .init(id: 14, name: "Max", position: "QA", mutual: 95, image: "02"),
    .init(id: 15, name: "Nancy", position: "Frontend Developer", mutual: 18, image: "03")

]

struct MyNetworkView: View {
    var body: some View {
        VStack{
            SearchBarView()
            HStack{
                Text("Manage My Network")
                    .font(.body)
                    .foregroundStyle(Color(.blue))
                Spacer()
                Image(systemName: "chevron.right")
            }
            .padding(.horizontal)
            Rectangle()
                .fill(.gray.opacity(0.4))
                .frame(width: .infinity, height: 10)
                .ignoresSafeArea(.all)
            HStack{
                Text("Invitations")
                    .font(.body)
                    .foregroundStyle(Color(.blue))
                Spacer()
                Image(systemName: "chevron.right")
            }
            .padding(.horizontal)
            Divider()
            ScrollView(.vertical, showsIndicators: false){
                ForEach(networkData, id: \.id){ item in
                    InvitationView(Data: item)
                    Divider()
                }
            }
        }
    }
}

#Preview {
    MyNetworkView()
}
