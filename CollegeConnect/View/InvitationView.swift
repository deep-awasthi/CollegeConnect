//
//  InvitationView.swift
//  CollegeConnect
//
//  Created by Deep Awasthi on 22/5/24.
//

import SwiftUI

let sampleData = NetworkModel(id: 0, name: "Deep", position: "SDE at Amazon", mutual: 10, image: "01")

struct InvitationView: View {
    var Data: NetworkModel
    var body: some View {
        HStack(alignment: .center ,spacing: 10){
            Image(Data.image)
                .resizable()
                .aspectRatio(contentMode: .fit)
                .clipShape(Circle())
                .frame(width: 80, height: 80, alignment: .center)
            VStack(alignment: .leading){
                Text(Data.name)
                    .font(.body)
                Text(Data.position)
                    .font(.subheadline)
                    .foregroundStyle(Color(.gray))
                Text("\(Data.mutual) mutual Connections")
                    .font(.caption)
                    .foregroundStyle(Color(.gray))
            }
            .frame(width: 150, height: 20, alignment: .leading)
            HStack{
                Image(systemName: "multiply.circle")
                    .font(.system(size: 35))
                    .foregroundStyle(Color(.gray))
                Image(systemName: "checkmark.circle")
                    .font(.system(size: 35))
                    .foregroundStyle(Color(.blue))
                    .opacity(0.8)
            }
            .padding(.horizontal)
        }
        .frame(width: .infinity, height: .infinity)
        .padding(.horizontal)
    }
}

#Preview {
    InvitationView(Data: sampleData)
}
