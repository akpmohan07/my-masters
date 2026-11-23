// SPDX-License-Identifier: MIT
pragma solidity ^0.8.0;

contract LotteryV1 {
    address[] public players;

    function enter() external payable {
        require(msg.value > 0, "need to sendETH");
        players.push(msg.sender);
    }

    function pickWinner() external {
        require(players.length > 0, "no players");
        uint256 idx = uint256(blockhash(block.number - 1)) % players.length;
        address winner = players[idx];
        payable(winner).transfer(address(this).balance);
        delete players;
    }
}
