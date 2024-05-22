//
//  SearchBarView.swift
//  CollegeConnect
//
//  Created by Deep Awasthi on 22/5/24.
//

import SwiftUI

struct SearchBarView: View {
    var body: some View {
        HStack(alignment: .center){
            Image("demo")
                .resizable()
                .ignoresSafeArea(.all)
                .aspectRatio(contentMode: .fit)
                .clipShape(Circle())
                .frame(width: 50, height: 50)
            RoundedRectangle(cornerRadius: 8)
                .foregroundColor(.gray.opacity(0.4))
                .frame(width: 270, height: 30)
                .overlay(
                    HStack(spacing: 10){
                        Image(systemName: "magnifyingglass")
                        Text("Search")
                            .font(.body)
                            .multilineTextAlignment(.leading)
                            .foregroundColor(.gray)
                        Spacer()
                    }
                        .padding()
                )
            Image(systemName: "ellipses.bubble.fill")
                .resizable()
                .foregroundStyle(.gray)
                .aspectRatio(contentMode: .fit)
                .frame(width: 30, height: 30)
        }
    }
}

#Preview {
    SearchBarView()
}
