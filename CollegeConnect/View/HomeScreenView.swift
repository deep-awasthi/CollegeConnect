//
//  HomeScreenView.swift
//  CollegeConnect
//
//  Created by Deep Awasthi on 22/5/24.
//

import SwiftUI

struct HomeScreenView: View {
    var body: some View {
        VStack{
            ProfileAndPostView()
            PostView()
        }
    }
}

#Preview {
    HomeScreenView()
}
